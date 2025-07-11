// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.yumdetail.tabs.instructions.component

import androidx.recyclerview.widget.DiffUtil
import com.ak.feastit.ui.yumdetail.StepSection

internal class InstructionDiffUtil : DiffUtil.ItemCallback<StepSection>() {
  override fun areItemsTheSame(
    oldItem: StepSection,
    newItem: StepSection,
  ): Boolean = oldItem.areItemsTheSame(newItem)

  override fun areContentsTheSame(
    oldItem: StepSection,
    newItem: StepSection,
  ): Boolean = oldItem.areContentsTheSame(newItem)
}
