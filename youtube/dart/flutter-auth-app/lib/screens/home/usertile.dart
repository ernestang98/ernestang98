import 'package:flutter_crud_firebase/models/registereduser.dart';
import 'package:flutter/material.dart';

class UserTile extends StatelessWidget {
  final RegisteredUser user;
  UserTile({this.user});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 8.0),
      child: Card(
        margin: EdgeInsets.fromLTRB(20.0, 6.0, 20.0, 0.0),
        child: ListTile(
          leading: CircleAvatar(
            radius: 25.0,
            backgroundImage: AssetImage('assets/coffee_icon.png'),
            backgroundColor: user.year > 0
                ? Colors.brown[user.year * 100]
                : Colors.brown[50],
          ),
          title: Text(user.name),
          subtitle: user.university == null
              ? Text('Fill in university details')
              : Text('Attends ${user.university}'),
        ),
      ),
    );
  }
}
