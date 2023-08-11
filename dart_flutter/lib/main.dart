import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:io';
import 'package:path_provider/path_provider.dart';

class DatabaseRecord {
  String email = "";
  String password = "";

  DatabaseRecord(this.email, this.password);

  DatabaseRecord.fromJson(Map<String, dynamic> json) {
    email = json['email'] ?? "";
    password = json['password'] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['email'] = email;
    data['password'] = password;
    return data;
  }
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  TextEditingController email = TextEditingController();
  TextEditingController password = TextEditingController();
  List<DatabaseRecord> database = [];

  void readFile() async {
    final temp = await getTemporaryDirectory();
    final fileUri = "${temp.path}/database.json";
    if (File(fileUri).existsSync()) {
      final raw = File(fileUri).readAsStringSync(encoding: utf8);
      if (raw.length == 0) {
        return;
      }
      final json = jsonDecode(raw);
      List<DatabaseRecord> temp = [];
      for (final record in json) {
        temp.add(DatabaseRecord.fromJson(record));
      }
      setState(() {
        database = temp;
      });
    } else {
      File(fileUri).createSync();
    }
  }

  void writeFile() async {
    final temp = await getTemporaryDirectory();
    final fileUri = "${temp.path}/database.json";
    File(fileUri).writeAsBytesSync(utf8.encode(jsonEncode(database)));
  }

  void showAlert(String text) {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(text),
    ));
  }

  void handleRegisterClick() {
    if (email.text.isEmpty || password.text.isEmpty) {
      showAlert("Preencha todos os campos");
      return;
    }
    if (!email.text.contains('@')) {
      showAlert("E-mail inválido");
      return;
    }
    for (final record in database) {
      if (email.text == record.email) {
        showAlert("Usuário já existe");
        return;
      }
    }
    final temp = DatabaseRecord(email.text, password.text);
    final dataTemp = [...database];
    dataTemp.add(temp);
    setState(() {
      database = dataTemp;
    });
    writeFile();
    email.text = "";
    password.text = "";
    showAlert("Usuário registrado");
  }

  void handleAuthenticateClick() {
    var control = false;
    for (final record in database) {
      if (record.email == email.text) {
        control = true;
        if (record.password == password.text) {
          email.text = "";
          password.text = "";
          showAlert("Usuário autenticado");
          return;
        }
        showAlert("Senha incorreta");
        return;
      }
    }
    if (!control) {
      showAlert("Usuário não encontrado");
    }
  }

  @override
  void initState() {
    super.initState();
    readFile();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: SizedBox(
          height: MediaQuery.of(context).size.height * .6,
          width: MediaQuery.of(context).size.width * .6,
          child: Card(
            child: Padding(
              padding: EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  TextFormField(
                    controller: email,
                    decoration: InputDecoration(
                      label: Text("E-mail"),
                    ),
                  ),
                  TextFormField(
                    controller: password,
                    decoration: InputDecoration(
                      label: Text("Senha"),
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.only(top: 16),
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                          backgroundColor: Theme.of(context).colorScheme.primary.withOpacity(.1)),
                      onPressed: handleAuthenticateClick,
                      child: Text("Autenticar"),
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.only(top: 16),
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                          backgroundColor: Theme.of(context).colorScheme.primary.withOpacity(.1)),
                      onPressed: handleRegisterClick,
                      child: Text("Registrar"),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
