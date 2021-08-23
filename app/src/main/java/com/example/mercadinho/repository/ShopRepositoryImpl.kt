package com.example.mercadinho.repository

import android.util.Log
import com.example.mercadinho.BuildConfig.FIREBASE_REALTIME_URL
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton


val GROUPS_KEY = "groups"
val ITEMS_KEY = "items"
val USER_KEY = "users"
val USER_GROUP_KEY = "User-Group"
val GROUP_USER_KEY = "Group-User"

@Singleton
class ShopRepositoryImpl @Inject constructor() : ShopGroupRepository, ShopItemRepository, ShopGroupDetailsRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_URL)
    private val groupsDbref: DatabaseReference = database.getReference(GROUPS_KEY)
    private val itemsDbref: DatabaseReference = database.getReference(ITEMS_KEY)
    val userDbref: DatabaseReference = database.getReference(USER_KEY)
    private val userGroup: DatabaseReference = database.getReference(USER_GROUP_KEY)
    val groupUser: DatabaseReference = database.getReference(GROUP_USER_KEY)
    var user: FirebaseUser? = null

    override fun initStuff(
        onGroupAdded: ((ShopGroup) -> Unit),
        onGroupChanged: ((ShopGroup) -> Unit),
        onGroupRemoved: ((String?) -> Unit)
    ) {
        //watch on user -> groups
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                this.user = user
                database.reference.child("$USER_GROUP_KEY/${user.uid}")
                    .childListener(onChildAdded = { snapShot, prv ->
                        snapShot.key?.let {
                            watchGroup(it)
                            val group = snapShot.value as HashMap<String, Any>
                            onGroupAdded(ShopGroup(it, group["name"] as String))
                        }

                    },
                        onChildChanged = { dataSnapshot, s ->
                            dataSnapshot.key?.let {
                                val groupMap = dataSnapshot.value as HashMap<String, Any>
                                onGroupChanged(ShopGroup(it, groupMap["name"] as String))
                            }
                        },
                        onChildRemoved = { dataSnapshot, event ->
                            onGroupRemoved(dataSnapshot.key)
                            groupsDbref.child(dataSnapshot.key!!).removeEventListener(event)
                        })
            }
        }
    }

    private fun watchGroup(key: String) {
        groupsDbref.child(key).valueEventListener {
            val value = it.value as Map<String, Any>
            userGroup.child(this.user!!.uid).child(key).setValue(
                mapOf(
                    "name" to value["name"]
                )
            )
        }
    }

    override fun addGroupFB(group: ShopGroup) {
        user?.let { user ->
            val newGroupId = groupsDbref.push().key ?: ""
            group.user = user.uid
            val map: HashMap<String, Any> = hashMapOf()
            map["$GROUPS_KEY/$newGroupId"] = group
            map["$GROUP_USER_KEY/$newGroupId/${user.uid}"] = user.displayName as Any //todo true
            map["$USER_GROUP_KEY/${user.uid}/$newGroupId"] = group
            database.reference.updateChildren(map)
        }
    }

    override fun removeGroupFB(group: ShopGroup) {
        user?.let { user ->
            val removeGroupId = group.id
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUPS_KEY/$removeGroupId"] = null
            map["$GROUP_USER_KEY/$removeGroupId"] = null
            map["$USER_GROUP_KEY/${user.uid}/$removeGroupId"] = null
            database.reference.updateChildren(map)
        }
    }

    override fun getItemByShopId(
        itemId: String,
        onUpdate: ((List<ShopItem>) -> Unit)?,
        onCanceled: ((DatabaseError) -> Unit)?
    ) {
        itemsDbref.child(itemId).valueEventListener(onUpdate = {
            it?.let {
                onUpdate?.invoke(it.map { map ->
                    ShopItem.fromMap(
                        map.key,
                        it[map.key] as HashMap<String, Any>
                    )
                })
            }
        }, onCanceled = onCanceled)
    }

    override fun addItem(item: ShopItem) {
        val id = groupsDbref.push().key
        id?.let {
            itemsDbref.child(item.groupId).child(it).setValue(item)
        }
    }

    override fun removeItemFB(item: ShopItem) {
        itemsDbref.child(item.id).removeValue()
    }

    override fun updateItem(item: ShopItem) {
        itemsDbref.child(item.groupId).child(item.id).child("bought").setValue(item.bought)
    }

    override fun joinGroup(groupId: String, failedToJoin: (() -> Unit)?) {
        user?.let { user ->
            database.reference.child(GROUPS_KEY).child(groupId).singleValueEvent(onSnapshot = {
                if (it.exists()) {
                    val map: HashMap<String, Any?> = hashMapOf()
                    map["$GROUP_USER_KEY/$groupId/${user.uid}"] = user.displayName
                    map["$USER_GROUP_KEY/${user.uid}/$groupId"] = ShopGroup(it.key!!, "nome")
                    database.reference.updateChildren(map)
                } else
                    failedToJoin?.invoke()
            })
        }
    }

    override fun leaveGroup(group: ShopGroup) {
        user?.let { user ->
            val joinGroupId = group.id
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUP_USER_KEY/$joinGroupId/${user.uid}"] = null
            map["$USER_GROUP_KEY/${user.uid}/$joinGroupId"] = null
            database.reference.updateChildren(map)
        }
    }
}


fun MutableStateFlow<MutableList<ShopGroup>>.remove(key: String?) {
    value.removeAll { it.id == key }
    value = value
}

fun <T> MutableStateFlow<MutableList<T>>.add(grupos: T) {
    value?.add(grupos)
    value = value
}

fun <T> MutableStateFlow<MutableList<T>>.add(index: Int, grupos: T) {
    value?.add(index, grupos)
    value = value
}


fun MutableStateFlow<MutableList<ShopGroup>>.addAfter(key: String?, group: ShopGroup) {
    val id = value.indexOfFirst {
        it.id == key
    }
    if (id >= 0)
        add(id + 1, group)
    else
        add(group)

}


fun Query.valueEventListener(
    onUpdate: ((Map<String, Any>?) -> Unit)? = null,
    onCanceled: ((DatabaseError) -> Unit)? = null,
    onSnapshot: ((DataSnapshot) -> Unit)? = null,
) {
    this.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.value
            onUpdate?.invoke(value as Map<String, Any>?)
            onSnapshot?.invoke(dataSnapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            onCanceled?.invoke(error)
            Log.w("shopGroupFrag", "Failed to read value.", error.toException())
        }
    })
}

fun Query.singleValueEvent(
    onUpdate: ((Map<String, Any>?) -> Unit)? = null,
    onCanceled: ((DatabaseError) -> Unit)? = null,
    onSnapshot: ((DataSnapshot) -> Unit)? = null,
) {
    this.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.value
            onUpdate?.invoke(value as HashMap<String, Any>?)
            onSnapshot?.invoke(dataSnapshot)
            Log.d("shopGroupFrag", "Value is: $value")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            onCanceled?.invoke(error)
            Log.w("shopGroupFrag", "Failed to read value.", error.toException())
        }
    })
}

fun Query.childListener(
    onChildAdded: ((DataSnapshot, String?) -> Unit)? = null,
    onChildChanged: ((DataSnapshot, String?) -> Unit)? = null,
    onChildRemoved: ((DataSnapshot, ChildEventListener) -> Unit)? = null,
    onChildMoved: ((DataSnapshot, String?) -> Unit)? = null,
    onCanceled: ((DatabaseError) -> Unit)? = null,
) {
    this.addChildEventListener(object : ChildEventListener {

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            onChildAdded?.invoke(snapshot, previousChildName)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            onChildChanged?.invoke(snapshot, previousChildName)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            onChildRemoved?.invoke(snapshot, this)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            onChildMoved?.invoke(snapshot, previousChildName)
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            onCanceled?.invoke(error)
            Log.w("shopGroupFrag", "Failed to read value.", error.toException())
        }
    })
}