package com.Puqbafu0.Sogsok.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.TextSecondary

@Composable
fun PauseDialog(
    levelIndex: Int,
    torches: Int,
    relicsFound: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(20.dp),
        containerColor = Surface1,
        titleContentColor = GoldLight,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.pause_body,
                    levelIndex + 1,
                    torches,
                    relicsFound
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.pause_resume),
                    style = MaterialTheme.typography.labelSmall,
                    color = Gold
                )
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onRestart) {
                    Text(
                        text = stringResource(R.string.pause_restart),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.action_menu),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    )
}
