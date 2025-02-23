package com.example.navegacao1.ui.telas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Usuario
import com.example.navegacao1.model.dados.UsuarioDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaCadastro(modifier: Modifier = Modifier, onSignupClick: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val usuarioDAO = UsuarioDAO()

    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") })
        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            if (nome.isNotBlank() && senha.isNotBlank()) {
                val novoUsuario = Usuario(nome = nome, senha = senha)
                scope.launch(Dispatchers.IO) {
                    usuarioDAO.adicionar(novoUsuario) { usuario ->
                        if (usuario != null) {
                            onSignupClick()
                        } else {
                            mensagemErro = "Erro ao cadastrar usuário."
                        }
                    }
                }
            } else {
                mensagemErro = "Preencha todos os campos."
            }
        }) {
            Text("Cadastrar")
        }

        mensagemErro?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                mensagemErro = null
            }
        }
    }
}
