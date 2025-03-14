package com.example.adminblinkit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkit.Constants
import com.example.adminblinkit.R
import com.example.adminblinkit.Utils
import com.example.adminblinkit.activity.AdminMainActivity
import com.example.adminblinkit.adapter.AdapterSelectedImage
import com.example.adminblinkit.databinding.FragmentAddProductBinding
import com.example.adminblinkit.model.Product
import com.example.adminblinkit.viewmodels.AdminViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val imageUri: ArrayList<Uri> = arrayListOf()
    val selectedImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { listOfUri ->
        val fiveImages = listOfUri.take(5)
        imageUri.clear()
        imageUri.addAll(fiveImages)

        binding.rvProductImages.adapter = AdapterSelectedImage(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        getStatusBarColor()
        setAutoCompleteTextViews()
        onImageSelectClicked()
        onAddButtonClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {

        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(), "Uploading images....")

            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() ||productUnit.isEmpty()
                || productPrice.isEmpty() || productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()) {
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Empty fields are not allowed")
                }
            } else if (imageUri.isEmpty()) {
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please upload images")
                }
            } else {

                val product = Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId = Utils.getRandomId()
                )

                saveImage(product)

            }
        }

    }

    private fun saveImage(product: Product) {

        viewModel.saveImagesInDB(imageUri)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect {
                if (it) {
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(), "imageSaved")
                    }
                    getUrls(product)
                }
            }
        }

    }

    private fun getUrls(product: Product) {

        Utils.showDialog(requireContext(), "Publishing product....")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect {
                val urls = it
                product.productImageUris = urls
                saveProduct(product)
            }
        }

    }

    private fun saveProduct(product: Product) {

        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect {
                if (it) {
                    Utils.hideDialog()
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Your product is live")
                }
            }
        }

    }

    private fun onImageSelectClicked() {
        binding.btnSelectImage.setOnClickListener {
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextViews() {

        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }

    }

    private fun getStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}