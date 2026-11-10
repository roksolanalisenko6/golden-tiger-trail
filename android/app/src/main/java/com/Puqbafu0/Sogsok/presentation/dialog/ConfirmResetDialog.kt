package com.Puqbafu0.Sogsok.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.TextSecondary

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(20.dp),
        containerColor = Surface1,
        titleContentColor = GoldLight,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.settings_reset_title),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 19.sp
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_reset_body),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.settings_confirm),
                    style = MaterialTheme.typography.labelSmall,
                    color = Ember
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(R.string.settings_cancel),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    )
}
