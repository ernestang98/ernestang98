// import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_crud_firebase/screens/home/usertile.dart';
import 'package:flutter_crud_firebase/models/registereduser.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class UserList extends StatefulWidget {
  @override
  _UserListState createState() => _UserListState();
}

class _UserListState extends State<UserList> {
  @override
  Widget build(BuildContext context) {
    // final brews = Provider.of<QuerySnapshot>(context);
    // for (var doc in brews.documents) {
    //   print(doc.data);
    // }

    final users = Provider.of<List<RegisteredUser>>(context) ?? [];

    // if (users != null) {
    //   users.forEach((user) {
    //     print(user.name);
    //     print(user.university);
    //     print(user.year);
    //   });
    // }

    // return Container();

    return ListView.builder(
        itemCount: users.length,
        itemBuilder: (context, index) {
          return UserTile(user: users[index]);
        });
  }
}
