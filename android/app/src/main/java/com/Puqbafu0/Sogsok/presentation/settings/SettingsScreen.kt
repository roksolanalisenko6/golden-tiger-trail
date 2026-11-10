package com.Puqbafu0.Sogsok.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Puqbafu0.Sogsok.R
import com.Puqbafu0.Sogsok.core.di.AppViewModelFactory
import com.Puqbafu0.Sogsok.core.theme.BgBase
import com.Puqbafu0.Sogsok.core.theme.Ember
import com.Puqbafu0.Sogsok.core.theme.Gold
import com.Puqbafu0.Sogsok.core.theme.GoldLight
import com.Puqbafu0.Sogsok.core.theme.Jade
import com.Puqbafu0.Sogsok.core.theme.OnGold
import com.Puqbafu0.Sogsok.core.theme.SlabHighlight
import com.Puqbafu0.Sogsok.core.theme.StoneCold
import com.Puqbafu0.Sogsok.core.theme.Surface1
import com.Puqbafu0.Sogsok.core.theme.Surface2
import com.Puqbafu0.Sogsok.core.theme.TextMuted
import com.Puqbafu0.Sogsok.core.theme.TextPrimary
import com.Puqbafu0.Sogsok.core.theme.TextSecondary
import com.Puqbafu0.Sogsok.core.ui.AppBackground
import com.Puqbafu0.Sogsok.core.ui.ScreenHeader
import com.Puqbafu0.Sogsok.core.ui.SecondaryButton
import com.Puqbafu0.Sogsok.core.ui.StoneCard
import com.Puqbafu0.Sogsok.domain.model.ExpeditionMode
import com.Puqbafu0.Sogsok.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    AppBackground(
        stops = listOf(BgBase, Surface1.copy(alpha = 0.9f), BgBase),
        artAlpha = 0.18f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.settings_title),
                onBack = onMenu
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StoneCard(modifier = Modifier.fillMaxWidth()) {
                    OptionTitle(
                        title = stringResource(R.string.settings_mode_title),
                        description = stringResource(R.string.settings_mode_desc)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ModeChoice(
                            text = stringResource(R.string.settings_mode_guided),
                            selected = state.mode == ExpeditionMode.GUIDED,
                            onClick = { viewModel.onModeSelected(ExpeditionMode.GUIDED) },
                            modifier = Modifier.weight(1f)
                        )
                        ModeChoice(
                            text = stringResource(R.string.settings_mode_explorer),
                            selected = state.mode == ExpeditionMode.EXPLORER,
                            onClick = { viewModel.onModeSelected(ExpeditionMode.EXPLORER) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                ToggleRow(
                    title = stringResource(R.string.settings_labels_title),
                    description = stringResource(R.string.settings_labels_desc),
                    checked = state.slabLabels,
                    onCheckedChange = { enabled -> viewModel.onSlabLabels(enabled) }
                )

                ToggleRow(
                    title = stringResource(R.string.settings_motion_title),
                    description = stringResource(R.string.settings_motion_desc),
                    checked = state.reducedMotion,
                    onCheckedChange = { enabled -> viewModel.onReducedMotion(enabled) }
                )

                OutlinedButton(
                    onClick = { viewModel.askReset() },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Ember.copy(alpha = 0.55f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Surface2,
                        contentColor = Ember
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_reset),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp
                    )
                }
            }

            SecondaryButton(
                text = stringResource(R.string.action_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            )
        }

        if (state.confirmReset) {
            ConfirmResetDialog(
                onConfirm = { viewModel.confirmReset() },
                onCancel = { viewModel.cancelReset() }
            )
        }
    }
}

@Composable
private fun OptionTitle(title: String, description: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        color = GoldLight
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 12.sp,
        color = TextSecondary
    )
}

@Composable
private fun ToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val stateLabel = stringResource(
        if (checked) R.string.settings_on else R.string.settings_off
    )
    StoneCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OptionTitle(title = title, description = description)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = OnGold,
                    checkedTrackColor = Gold,
                    checkedBorderColor = Gold,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = StoneCold,
                    uncheckedBorderColor = SlabHighlight
                )
            )
        }
    }
}

@Composable
private fun ModeChoice(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stateLabel = stringResource(
        if (selected) R.string.settings_on else R.string.settings_off
    )
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (selected) Jade else SlabHighlight),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Jade else Surface2,
            contentColor = if (selected) OnGold else TextPrimary
        ),
        modifier = modifier
            .height(48.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}
