package com.example.mercadinho.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.repository.entities.teste.Grupos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton


val GROUPS_KEY = "groups"
val ITEMS_KEY = "items"
val USER_KEY = "users"
val USER_GROUP_KEY = "User-Group"
val GROUP_USER_KEY = "Group-User"

@Singleton
class ShopRepository @Inject constructor() {
    val database: FirebaseDatabase
    val groupsDbref: DatabaseReference
    val itemsDbref: DatabaseReference
    val userDbref: DatabaseReference
    val userGroup: DatabaseReference
    val groupUser: DatabaseReference
    var user: FirebaseUser? = null

    val groups: MutableLiveData<MutableList<Grupos>> =
        MutableLiveData<MutableList<Grupos>>().apply { value = mutableListOf() }

    init {
        database =
            FirebaseDatabase.getInstance("https://lista-de-compras-c0412-default-rtdb.firebaseio.com/")
        groupsDbref = database.getReference(GROUPS_KEY)
        itemsDbref = database.getReference(ITEMS_KEY)
        userDbref = database.getReference(USER_KEY)
        userGroup = database.getReference(USER_GROUP_KEY)
        groupUser = database.getReference(GROUP_USER_KEY)
        teste()
    }

    fun teste() {
        //watch on user -> groups
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                this.user = user
                database.reference.child("$USER_GROUP_KEY/${user.uid}")
                    .childListener(onChildAdded = { snapShot, prv ->
                        watchGroup(snapShot.key!!)
                        val group = snapShot.value as HashMap<String, Any>
                        groups.add(Grupos(snapShot.key!!, group["name"] as String))//value["name"] as String))
                    },
                    onChildChanged = { dataSnapshot, s ->
                        val group = groups.value?.first { dataSnapshot.key == it.groupId }
                        val groupMap = dataSnapshot.value as HashMap<String, Any>
                        group?.groupName = groupMap["name"] as String
                        groups.value = groups.value
                    },
                    onChildRemoved = { dataSnapshot, event ->
                        groups.remove(dataSnapshot.key!!)
                        groupsDbref.child(dataSnapshot.key!!).removeEventListener(event)
                    })
            }



//                userDbref.child(user.uid).child(GROUPS_KEY)
//                    .valueEventListener(onSnapshot = { snapShot ->
//                        snapShot.children.forEach { child ->
//                            child.key?.let {
////                                watchGroup(it)
//                                it
//                            }
//                        }
//                        groupsDbref.childListener(onChildAdded = { snapShot, prv ->
//                            val value = snapShot.value as Map<String, Any>
//                            groups?.addAfter(prv, Grupos(snapShot.key!!, value["name"] as String))
//                        })
//                    })
//            }
        }
    }

    fun MutableLiveData<MutableList<Grupos>>.addAfter(key: String?, group: Grupos) {
        val id = value?.indexOfFirst {
            it.groupId == key
        }
        if (id != null) {
            if (id >= 0)
                add(id + 1, group)
            else
                add(group)
        }
    }

    private fun watchGroup(key: String) {
        groupsDbref.child(key).valueEventListener {
            val value = it.value as Map<String, Any>
//            groups.add(Grupos(key, value["name"] as String))
            userGroup.child(this.user!!.uid).child(key).setValue(
                mapOf(
                   "name" to value["name"]
                )
            ) //= Grupos(key, value["name"] as String)
        }
    }


    fun getAllShops(
        onUpdate: ((HashMap<String, HashMap<String, Any>>?) -> Unit)? = null,
        onCanceled: ((DatabaseError) -> Unit)? = null
    ) {

//        FirebaseAuth.getInstance().addAuthStateListener {
//            it.currentUser?.let { user ->
//                this.user = user
//                groupsDbref//.orderByChild("user").equalTo(it.uid)
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            // This method is called once with the initial value and again
//                            // whenever data at this location is updated.
//                            val value = dataSnapshot.value
//                            onUpdate?.invoke(value as HashMap<String, HashMap<String, Any>>?)
//                            Log.d("shopGroupFrag", "Value is: $value")
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            // Failed to read value
//                            onCanceled?.invoke(error)
//                            Log.w("shopGroupFrag", "Failed to read value.", error.toException())
//                        }
//                    })
//            }
//        }

    }

    fun addGroupFB(group: ShopGroup) {
        user?.let { user ->
            val newGroupId = groupsDbref.push().key ?: ""
            group.user = user.uid
            val map: HashMap<String, Any> = hashMapOf()
            map["$GROUPS_KEY/$newGroupId"] = group
            map["$GROUP_USER_KEY/$newGroupId/${user.uid}"] = user.displayName as Any //todo true
            map["$USER_GROUP_KEY/${user.uid}/$newGroupId"] = true
            database.reference.updateChildren(map)
        }
    }

    fun removeGroupFB(group: ShopGroup) {
        user?.let { user ->
            val removeGroupId = group.id
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUPS_KEY/$removeGroupId"] = null
            map["$GROUP_USER_KEY/$removeGroupId"] = null
            map["$USER_GROUP_KEY/${user.uid}/$removeGroupId"] = null
            database.reference.updateChildren(map)
        }
    }

    fun getItemByShopId(
        groupId: String, onUpdate: ((Map<String, Any>?) -> Unit)? = null,
        onCanceled: ((DatabaseError) -> Unit)? = null
    ) {
        itemsDbref.child(groupId).valueEventListener(onUpdate = onUpdate, onCanceled = onCanceled)
    }

    fun addItem(item: ShopItem) {
        val id = groupsDbref.push().key ?: "adadasfsdf"
        itemsDbref.child(item.groupId).child(id).setValue(item)
    }

    fun removeItemFB(item: ShopItem) {
        itemsDbref.child(item.id).removeValue()
    }

    fun updateItemFB(item: ShopItem) {
        itemsDbref.child(item.id).child("bought").setValue(item.bought)
    }

    fun joinGroup(groupId: String, failedToJoin: (() -> Unit)? = null) {
        user?.let { user ->
            database.reference.child(GROUPS_KEY).child(groupId).singleValueEvent(onSnapshot = {
                if(it.exists()) {
                    val map: HashMap<String, Any?> = hashMapOf()
                    map["$GROUP_USER_KEY/$groupId/${user.uid}"] = user.displayName
                    map["$USER_GROUP_KEY/${user.uid}/$groupId"] = true
                    database.reference.updateChildren(map)
                } else
                    failedToJoin?.invoke()
            })
        }
    }

    fun leaveGroup(group: ShopGroup) {
        user?.let { user ->
            val joinGroupId = group.id
            val map: HashMap<String, Any?> = hashMapOf()
            map["$GROUP_USER_KEY/$joinGroupId/${user.uid}"] = null
            map["$USER_GROUP_KEY/${user.uid}/$joinGroupId"] = null
            database.reference.updateChildren(map)
        }
    }
}


private fun MutableLiveData<MutableList<Grupos>>.remove(key: String?) {
        value?.removeAll { it.groupId == key }
        value = value
}

private fun <T> MutableLiveData<MutableList<T>>.add(grupos: T) {
    value?.add(grupos)
    value = value
}

private fun <T> MutableLiveData<MutableList<T>>.add(index: Int, grupos: T) {
    value?.add(index, grupos)
    value = value
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
            Log.d("shopGroupFrag", "Value is: $value")
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