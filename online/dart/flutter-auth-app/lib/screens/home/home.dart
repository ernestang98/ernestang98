import 'package:flutter/material.dart';
import 'package:flutter_crud_firebase/models/registereduser.dart';
import 'package:flutter_crud_firebase/services/auth.dart';
import 'package:flutter_crud_firebase/services/database.dart';
import 'package:provider/provider.dart';
// import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_crud_firebase/screens/home/userlist.dart';
import 'package:flutter_crud_firebase/screens/home/settings.dart';

class Home extends StatelessWidget {
  final AuthService _auth = AuthService();

  @override
  Widget build(BuildContext context) {
    void _showSettingsPanel() {
      showModalBottomSheet(
          context: context,
          builder: (context) {
            return Container(
                padding: EdgeInsets.symmetric(vertical: 20.0, horizontal: 60.0),
                child: SettingsForm());
          });
    }

    return StreamProvider<List<RegisteredUser>>.value(
      // return StreamProvider<QuerySnapshot>.value(
      value: DatabaseService().users,
      child: Scaffold(
        backgroundColor: Colors.brown[50],
        appBar: AppBar(
          title: Text('User List'),
          backgroundColor: Colors.brown[400],
          elevation: 0.0,
          actions: <Widget>[
            TextButton.icon(
              style: ButtonStyle(
                backgroundColor:
                    MaterialStateProperty.all<Color>(Colors.brown[400]),
                foregroundColor: MaterialStateProperty.all<Color>(Colors.white),
              ),
              icon: Icon(Icons.person),
              label: Text('logout'),
              onPressed: () async {
                await _auth.signOut();
              },
            ),
            TextButton.icon(
              style: ButtonStyle(
                backgroundColor:
                    MaterialStateProperty.all<Color>(Colors.brown[400]),
                foregroundColor: MaterialStateProperty.all<Color>(Colors.white),
              ),
              icon: Icon(Icons.settings),
              label: Text('settings'),
              onPressed: () => _showSettingsPanel(),
            )
          ],
        ),
        body: Container(
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage('assets/coffee_bg.png'),
                fit: BoxFit.cover,
              ),
            ),
            child: UserList()),
      ),
    );
  }
}
