package com.example.mundim.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mundim.Activities.NewSampleActivity
import com.example.mundim.Activities.PatientActivity
import com.example.mundim.Activities.PictureActivity
import com.example.mundim.Activities.SampleActivity
import com.example.mundim.Classes.Primary.Sample
import com.example.mundim.R
import com.example.mundim.Services.GET_EDITED_SAMPLE_DATA
import com.example.mundim.Services.GET_NEW_SAMPLE_DATA
import kotlinx.android.synthetic.main.fragment_samples.view.*
import kotlinx.android.synthetic.main.sample_ticket.view.*

class AmostrasFragment : Fragment() {

    private var listener: SampleFragmentInterface? = null
    lateinit var samplesAdapter: SampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_samples, container, false)
        listener!!.setLoadingTextView(view.loadingTextView)
        view.samplesListView.adapter = samplesAdapter
        view.newSampleBtn.setOnClickListener {
            val intent = Intent(context, NewSampleActivity::class.java)
            intent.putExtra("db_id", activity!!.intent.extras.getString("db_id"))
            intent.putExtra("nome", activity!!.intent.extras.getString("nome"))
            activity!!.startActivityForResult(intent, GET_NEW_SAMPLE_DATA)
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SampleFragmentInterface) {
            listener = context
            samplesAdapter = SampleAdapter(activity!!, listener!!.getSamplesList())
            listener?.setAdapter(samplesAdapter)
        } else {
            throw RuntimeException(context.toString() + " must implement SampleFragmentInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface SampleFragmentInterface {
        fun getSamplesList():ArrayList<Sample>
        fun setAdapter(adapter: SampleAdapter)
        fun setLoadingTextView(textView: TextView)
    }
}

class SampleAdapter: BaseAdapter {
    var samples = ArrayList<Sample>()
    var context: Context? = null
    constructor(context: Context, samples: ArrayList<Sample>):super(){
        this.samples = samples
        this.context = context
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val sample = samples[position]
        var inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var myView = inflator.inflate(R.layout.sample_ticket, null)
        myView.setOnClickListener{
            val intent = Intent(context, SampleActivity::class.java)
            intent.putExtra("url", sample.url)
            intent.putExtra("id", sample.id)
            intent.putExtra("area_anatomica", sample.area_anatomica)
            intent.putExtra("antecedentes_familiares", sample.antecedentes_familiares)
            intent.putExtra("diagnostico_dermatologista", sample.diagnostico_dermatologista)
            intent.putExtra("diagnostico_biopsia", sample.diagnostico_biopsia)
            intent.putExtra("position", position.toString())
            (context as PatientActivity).startActivityForResult(intent, GET_EDITED_SAMPLE_DATA)
        }
        myView.nomeTextView.text = sample.nome + ", #" + (position + 1)
        myView.dataTextView.text = sample.data
        return myView
    }

    override fun getItem(position: Int): Any {
        return samples[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return samples.size
    }

}

