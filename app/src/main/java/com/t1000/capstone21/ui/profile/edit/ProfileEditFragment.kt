package com.t1000.capstone21.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.t1000.capstone21.R
import com.t1000.capstone21.databinding.ProfileEditFragmentBinding
import com.t1000.capstone21.utils.getScaledBitmap

private const val TAG = "ProfileEditFragment"
class ProfileEditFragment : Fragment() {

 private lateinit var binding :ProfileEditFragmentBinding
    private var selectedPhotoUri: Uri? = null

    private val viewModel by lazy { ViewModelProvider(this).get(ProfileEditViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = ProfileEditFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.profilePicBtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.uplod.setOnClickListener {
            selectedPhotoUri?.let {
                viewModel.uploadProfilePhoto(it) }

        }
//            val action = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment(
//                FirebaseAuth.getInstance().currentUser?.uid)
//           findNavController().navigate(action)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data ?: return

            binding.profilePicBtn2.visibility = View.GONE
            binding.profilePicView2.load(selectedPhotoUri)



        }
    }

}