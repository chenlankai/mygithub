package com.example.myapplication
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.fragment.app.Fragment
import com.example.listeners.OnFragmentInteractionListener

class SecondFragment : Fragment(){
    private var param1: String? = null
    private var param2: Int? = null
    private var listener: OnFragmentInteractionListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnFragmentInteractionListener
        if (listener == null) {
            Log.d("SecondFragment","$context 必须要实现 OnFragmentInteractionListener 接口")
            throw ClassCastException("$context 必须要实现 OnFragmentInteractionListener 接口")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<Button>(R.id.btn_send_data)
        btn.setOnClickListener {
            // 3. 点击按钮时调用接口方法，向 Activity 发送数据
            Log.d("SecondFragment","向MainActivity发送数据")
            listener?.onFragmentInteraction(" SecondFragment 的数据发送数据")

        }
    }
    companion object{
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

}