package ru.qwonix.android.foxwhiskers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.qwonix.android.foxwhiskers.R
import ru.qwonix.android.foxwhiskers.databinding.FragmentPhoneNumberInputBinding
import ru.qwonix.android.foxwhiskers.util.focusAndShowKeyboard
import ru.tinkoff.decoro.FormattedTextChangeListener
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class PhoneNumberInputFragment : Fragment(R.layout.fragment_phone_number_input) {

    private lateinit var binding: FragmentPhoneNumberInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberInputBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            isMaskFilled = false
            hasError = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneNumberTextView.focusAndShowKeyboard()

        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(binding.phoneNumberTextView)

        watcher.setCallback(object : FormattedTextChangeListener {
            override fun beforeFormatting(oldValue: String?, newValue: String?): Boolean {
                return false
            }

            override fun onTextFormatted(formatter: FormatWatcher, newFormattedText: String?) {
                if (formatter.mask.filled()) {
                    binding.hasError = false
                    binding.isMaskFilled = true
                }
            }
        })


        binding.sendCodeButton.setOnClickListener {
            if (binding.isMaskFilled == true) {
                findNavController().navigate(R.id.action_phoneNumberInputFragment_to_phoneNumberConfirmationFragment)
            } else {
                binding.hasError = true
            }
        }
    }
}