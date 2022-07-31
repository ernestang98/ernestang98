import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_crud_firebase/models/registereduser.dart';

class DatabaseService {
  final String uid;
  DatabaseService({this.uid});

  // collection reference
  final CollectionReference userCollection =
      Firestore.instance.collection('users');

  Future<void> updateUserData(String name, String university, int year) async {
    return await userCollection
        .document(uid)
        .setData({'name': name, 'university': university, 'year': year});
  }

  List<RegisteredUser> _userListFromSnapshot(QuerySnapshot snapshot) {
    return snapshot.documents.map((doc) {
      //print(doc.data);
      return RegisteredUser(
          name: doc.data['name'] ?? 'John Doe',
          university: doc.data['university'] ?? null,
          year: doc.data['year'] ?? '-1');
    }).toList();
  }

  RegisteredUser _userDataFromSnapshot(DocumentSnapshot snapshot) {
    return RegisteredUser(
        name: snapshot.data['name'],
        university: snapshot.data['university'],
        year: snapshot.data['year']);
  }

  // get user doc stream
  Stream<RegisteredUser> get userData {
    return userCollection.document(uid).snapshots().map(_userDataFromSnapshot);
  }

  // Stream<QuerySnapshot> get users {
  //   return userCollection.snapshots();
  // }

  Stream<List<RegisteredUser>> get users {
    return userCollection.snapshots().map(_userListFromSnapshot);
  }
}
