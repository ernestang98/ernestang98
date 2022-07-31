import 'package:flutter_crud_firebase/shared/constants.dart';
import 'package:flutter/material.dart';
import 'package:flutter_crud_firebase/models/user.dart';
import 'package:flutter_crud_firebase/models/registereduser.dart';
import 'package:flutter_crud_firebase/services/database.dart';
import 'package:flutter_crud_firebase/shared/loading.dart';
import 'package:provider/provider.dart';

class SettingsForm extends StatefulWidget {
  @override
  _SettingsFormState createState() => _SettingsFormState();
}

class _SettingsFormState extends State<SettingsForm> {
  final _formKey = GlobalKey<FormState>();
  final List<String> universities = [
    'NTU',
    'NUS',
    'SMU',
    'SUSS',
    'SUTD',
    'Others'
  ];

  // form values
  String _currentName;
  String _currentUniversity;
  int _currentYear = 1;

  @override
  Widget build(BuildContext context) {
    User user = Provider.of<User>(context);

    return StreamBuilder<RegisteredUser>(
        stream: DatabaseService(uid: user.uid).userData,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            RegisteredUser userData = snapshot.data;
            return Form(
              key: _formKey,
              child: Column(
                children: <Widget>[
                  Text(
                    'Update your account settings.',
                    style: TextStyle(fontSize: 18.0),
                  ),
                  SizedBox(height: 20.0),
                  TextFormField(
                    initialValue: userData.name,
                    decoration: textInputDecoration.copyWith(hintText: 'Name'),
                    validator: (val) =>
                        val.isEmpty ? 'Please enter a name' : null,
                    onChanged: (val) {
                      setState(() => _currentName = val);
                    },
                  ),
                  SizedBox(height: 10.0),
                  DropdownButtonFormField(
                    value: universities.contains(userData.university)
                        ? userData.university
                        : 'NTU',
                    decoration: textInputDecoration,
                    items: universities.map((uni) {
                      return DropdownMenuItem(
                        value: uni,
                        child: Text('$uni'),
                      );
                    }).toList(),
                    onChanged: (val) =>
                        setState(() => _currentUniversity = val),
                  ),
                  SizedBox(height: 10.0),
                  Slider(
                    value: (_currentYear < 1.0 ? 1.0 : _currentYear).toDouble(),
                    activeColor: Colors.brown[100],
                    inactiveColor: Colors.brown[300],
                    min: 1,
                    max: 9,
                    divisions: 8,
                    onChanged: (val) =>
                        setState(() => _currentYear = val.round()),
                  ),
                  Text('Year: ' + _currentYear.toString()),
                  SizedBox(height: 10.0),
                  RaisedButton(
                      color: Colors.pink[400],
                      child: Text(
                        'Update',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () async {
                        if (_formKey.currentState.validate()) {
                          await DatabaseService(uid: user.uid).updateUserData(
                              _currentName ?? snapshot.data.name,
                              _currentUniversity ?? snapshot.data.university,
                              _currentYear ?? snapshot.data.year);
                          Navigator.pop(context);
                        }
                      }),
                ],
              ),
            );
          } else {
            return Loading();
          }
        });
  }
}
