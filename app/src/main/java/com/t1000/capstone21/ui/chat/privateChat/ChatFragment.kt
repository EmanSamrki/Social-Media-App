package com.t1000.capstone21.ui.chat.privateChat

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.t1000.capstone21.databinding.ChatFragmentBinding
import com.t1000.capstone21.databinding.ItemChatReciverBinding
import com.t1000.capstone21.databinding.ItemChatSenderBinding
import com.t1000.capstone21.databinding.ItemVideoCommentBinding

import com.t1000.capstone21.models.ChatMessage
import com.t1000.capstone21.models.User
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ChatFragment"
class ChatFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }


    private lateinit var binding : ChatFragmentBinding

    private val args: ChatFragmentArgs by navArgs()

    private lateinit var senderId :String

    private lateinit var receiverId :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (FirebaseAuth.getInstance().currentUser?.uid == null){
            val action = ChatFragmentDirections.actionNavigationIndexToNavigationMe()
            findNavController().navigate(action)
         }else{
             senderId = FirebaseAuth.getInstance().currentUser?.uid!!
            if (args.chatReceivedId != null){
                receiverId = args.chatReceivedId.toString()
            }

        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatFragmentBinding.inflate(layoutInflater)

        binding.recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadChatMessages(senderId,receiverId).observe( viewLifecycleOwner
            ) {
                Log.e(TAG, "onViewCreated: sender = $senderId, receiver =$receiverId",)
                binding.recycler.adapter = ChatAdapter(it)
            }
        }


        //send message on keyboard done click
        binding.messageEditText.setOnEditorActionListener { _, actionId, _ ->
            sendMessage()
            true
        }
    }


    private fun sendMessage() {
        if (binding.messageEditText.text.isEmpty()) {
            Toast.makeText(context, "Empty String", Toast.LENGTH_LONG).show()
            return
        }
        val chatMessage = ChatMessage(senderId = senderId,
                                      receiverId = receiverId,
                                      text = binding.messageEditText.text.toString(),
                                      created_at = Timestamp(Date()))
        viewModel.sendMessage(chatMessage)

        binding.messageEditText.setText("")
    }




    private inner class ChatHolder(val bindingRec:ItemChatReciverBinding,val bindingSend:ItemChatSenderBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage){
            if (FirebaseAuth.getInstance().currentUser?.uid == chatMessage.senderId){
                bindingSend.dateTextView.text = chatMessage.text
            }else if (FirebaseAuth.getInstance().currentUser?.uid == chatMessage.receiverId){
                bindingRec.dateTextView.text = chatMessage.text
            }

        }



    }


    private inner class ChatAdapter(val messages:List<ChatMessage>):
        RecyclerView.Adapter<ChatHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ):ChatHolder {
            val bindingSend = ItemChatSenderBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            val bindingRec = ItemChatReciverBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return ChatHolder(bindingRec,bindingSend)

        }

        override fun onBindViewHolder(holder: ChatHolder, position: Int) {
            val chatItem: ChatMessage = messages[position]
            holder.bind(chatItem)
        }

        override fun getItemCount(): Int = messages.size






    }


}