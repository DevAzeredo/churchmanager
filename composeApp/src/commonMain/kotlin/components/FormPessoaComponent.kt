package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.Pessoa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPessoaComponent(
    onAddPessoa: (Pessoa) -> Unit, onClearClick: () -> Unit, pessoa: Pessoa? = null
) {
    var novoNome by remember { mutableStateOf(pessoa?.nome ?: "") }
    var novoTelCel by remember { mutableStateOf(pessoa?.telefone ?: "") }
    var novoEndereco by remember { mutableStateOf(pessoa?.endereco ?: "") }
    var novoProfissao by remember { mutableStateOf(pessoa?.profissao ?: "") }
    var novoEmail by remember { mutableStateOf(pessoa?.email ?: "") }
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    Column {
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedTextField(
            value = novoNome,
            onValueChange = { novoNome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoTelCel,
            onValueChange = { novoTelCel = it },
            label = { Text("Tel/Cel") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoEmail,
            onValueChange = { novoEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoProfissao,
            onValueChange = { novoProfissao = it },
            label = { Text("Profissão") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = novoEndereco,
            onValueChange = { novoEndereco = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        DateInputComponent(state)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val novaPessoa = Pessoa(
                        0, novoNome, state.selectedDateMillis?.let {
                            val instant = Instant.fromEpochMilliseconds(it)
                            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                            "${
                                dateTime.dayOfMonth.toString().padStart(2, '0')
                            }-${
                                dateTime.monthNumber.toString().padStart(2, '0')
                            }-${dateTime.year}"
                        }!!, novoEndereco, novoTelCel, novoEmail, novoProfissao
                    )
                    onAddPessoa(novaPessoa)
                    novoNome = ""
                    state.selectedDateMillis?.let {
                        val instant = Instant.fromEpochMilliseconds(it)
                        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                        "${
                            dateTime.dayOfMonth.toString().padStart(2, '0')
                        }-${
                            dateTime.monthNumber.toString().padStart(2, '0')
                        }-${dateTime.year}"
                    }!!
                    novoEndereco = ""
                    novoTelCel = ""
                    novoEmail = ""
                    novoProfissao = ""
                }, modifier = Modifier.weight(1f)
            ) {
                Text("Adicionar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onClearClick, modifier = Modifier.weight(1f)
            ) {
                Text("Limpar")
            }
        }
    }
}