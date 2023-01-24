package com.colodori.mercadinho.repository

import android.util.Log
import com.colodori.mercadinho.BuildConfig.FIREBASE_REALTIME_URL
import com.colodori.mercadinho.repository.entities.ShopGroup
import com.colodori.mercadinho.repository.entities.ShopItem
import com.colodori.mercadinho.repository.entities.UserInfo
import com.colodori.mercadinho.repository.local.LocalSharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow


val GROUPS_KEY = "groups"
val ITEMS_KEY = "items"
val USER_KEY = "users"
val USER_GROUP_KEY = "User-Group"
val GROUP_USER_KEY = "Group-User"

@Singleton
class ShopRepositoryImpl @Inject constructor() : ShopGroupRepository, ShopItemRepository, ShopGroupDetailsRepository, EditItemRepository {
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
                            group[ShopGroup::id.name] = it
                            onGroupAdded(ShopGroup.fromMap(group))
                        }

                    },
                        onChildChanged = { dataSnapshot, s ->
                            dataSnapshot.key?.let {
                                val groupMap = dataSnapshot.value as HashMap<String, Any>
                                groupMap[ShopGroup::id.name] = it
                                onGroupChanged(ShopGroup.fromMap(groupMap))
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
            val value = it.value as Map<String, Any>?
            value ?.let {
                userGroup.child(this.user!!.uid).child(key).setValue(
                    mapOf(
                        ShopGroup::name.name to value[ShopGroup::name.name]
                    )
                )
            }
        }
    }

    override fun addGroupFB(group: ShopGroup, userInfo: UserInfo) {
        user?.let { user ->
            val newGroupId = groupsDbref.push().key ?: ""
            val completeInfo = userInfo.copy(nickName = LocalSharedPref.userName, id = user.uid, name = user.displayName, profileUrl = user.photoUrl?.toString())
            group.user = user.uid
            val map: HashMap<String, Any> = hashMapOf()
            map["$GROUPS_KEY/$newGroupId"] = group
            map["$GROUP_USER_KEY/$newGroupId/${user.uid}"] = completeInfo
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
                onUpdate?.invoke(it?.map { map ->
                    ShopItem.fromMap(
                        map.key,
                        it[map.key] as HashMap<String, Any>
                    )
                } ?: emptyList())
        }, onCanceled = onCanceled)
    }

    override fun addItem(item: ShopItem) {
        val id = groupsDbref.push().key
        id?.let {
            item.id = it
            itemsDbref.child(item.groupId).child(it).setValue(item)
        }
    }

    override fun removeItemFB(item: ShopItem) {
        itemsDbref.child(item.id).removeValue()
    }

    override fun updateItem(item: ShopItem) {
        itemsDbref.child(item.groupId).child(item.id).child("bought").setValue(item.bought)
    }

    override fun joinGroup(groupId: String, userInfo: UserInfo, failedToJoin: (() -> Unit)?) {
        user?.let { user ->
            database.reference.child(GROUPS_KEY).child(groupId).singleValueEvent(onSnapshot = {
                if (it.exists()) {
                    val map: HashMap<String, Any?> = hashMapOf()
                    val completeInfo = userInfo.copy(nickName = LocalSharedPref.userName, id = user.uid, name = user.displayName, profileUrl = user.photoUrl?.toString())
                    map["$GROUP_USER_KEY/$groupId/${user.uid}"] = completeInfo
                    map["$USER_GROUP_KEY/${user.uid}/$groupId"] = ShopGroup(it.key!!, "nome")//TODO PEGAR O RESTO
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

    override fun editGroupDescription(group: ShopGroup, description: String) {
        user?.let {
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUPS_KEY/${group.id}/${ShopGroup::description.name}"] = description
            database.reference.updateChildren(map)
        }
    }

    override fun editGroupName(group: ShopGroup, name: String) {
        user?.let {
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUPS_KEY/${group.id}/${ShopGroup::name.name}"] = name
            database.reference.updateChildren(map)
        }
    }

    override fun removeUser(group: ShopGroup, user: UserInfo) {
        this.user?.let {
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUP_USER_KEY/${group.id}/${user.id}"] = null
//            map["$USER_GROUP_KEY/${user.id}/${group.id}"] = null
            database.reference.updateChildren(map)
        }
    }

    override fun giveAdmin(group: ShopGroup, user: UserInfo) {
        this.user?.let {
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUP_USER_KEY/${group.id}/${user.isAdmin}"] = true
            database.reference.updateChildren(map)
        }
    }

    override fun getGroup(group: ShopGroup,
                          onGroupDetailsUpdated: ((ShopGroup) -> Unit),
                          onGroupParticipantsUpdated: ((List<UserInfo>) -> Unit)) {
            user?.let {
                database.reference.child(GROUPS_KEY).child(group.id).valueEventListener(
                    onUpdate = { map ->
                        map?.let {
                            onGroupDetailsUpdated(ShopGroup.fromMap(it as HashMap<String, Any>))
                        }
                    }
                )

                database.reference.child(GROUP_USER_KEY).child(group.id).valueEventListener(
                    onUpdate = { map ->
                        //TODO MELHORAR ISSO
                        map?.let {
                            val part = it.map { userMap ->
                                val user = it[userMap.key] as Map<String, Any>
                                UserInfo(
                                    id = user[UserInfo::id.name] as String?,
                                    name = user[UserInfo::name.name] as String?,
                                    nickName = user[UserInfo::nickName.name] as String?,
                                    profileUrl = user[UserInfo::profileUrl.name] as String?,
                                    isAdmin = user[UserInfo::isAdmin.name] as Boolean? ?: false
                                )
                            }
                            onGroupParticipantsUpdated(part)
                        }
                    }
                )
            }
    }

    override fun updateItem(item: ShopItem, onUpdated: (() -> Unit)?) {
        itemsDbref.child(item.groupId).child(item.id).child(ShopItem::name.name).setValue(item.name)
        onUpdated?.invoke()
    }

    override fun removeItem(item: ShopItem, onRemoved: (() -> Unit)?) {
        itemsDbref.child(item.groupId).child(item.id).removeValue()
        onRemoved?.invoke()
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