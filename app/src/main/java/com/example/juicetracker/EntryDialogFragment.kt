import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.R.layout
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import androidx.fragment.app.viewModels
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel

class EntryDialogFragment : BottomSheetDialogFragment() {

    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEntryDialogBinding.inflate(inflater, container, false).root
    }

    var selectedColor: JuiceColor = JuiceColor.Red

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val colorLabelMap = JuiceColor.values().associateBy { getString(it.label) }
        val binding = FragmentEntryDialogBinding.bind(view)
        val juiceId = arguments?.getLong("itemId", 0L) ?: 0L

        binding.colorSpinner.adapter = ArrayAdapter(
            requireContext(),
            layout.support_simple_spinner_dropdown_item,
            colorLabelMap.map { it.key }
        )

        binding.colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // Get the label of the selected item
                val selected = parent.getItemAtPosition(pos).toString()
                // Get the enum value from string
                selectedColor = colorLabelMap[selected] ?: selectedColor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //if nothing is selected, assign the first color choice as the selectedColor
                selectedColor = JuiceColor.Red
            }
        }

        binding.saveButton.setOnClickListener {
            entryViewModel.saveJuice(
                juiceId,
                binding.name.text.toString(),
                binding.description.text.toString(),
                selectedColor.name,
                binding.ratingBar.rating.toInt()
            )
            dismiss()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}