package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
class AnotherFragment : Fragment(){
    private var param1:String?=null
    private var param2:Int?=null
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let{
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater:LayoutInflater,
        container:ViewGroup?,
        savedInstanceState:Bundle?
    ):View?{
        return inflater.inflate(R.layout.fragment_another,container,false)
    }
    companion object{
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1:String,param2:Int):AnotherFragment {
            return AnotherFragment().apply{
                arguments = Bundle().apply{
                    putString(ARG_PARAM1,param1)
                    putInt(ARG_PARAM2,param2)
                }
            }
        }
    }
}
