package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;

import java.util.List;

public class ContactDetailsAdapter extends RecyclerView.Adapter<ContactDetailsAdapter.ContactViewHolder> {

    private Context context;
    private List<ContactDetails> contactDetails;
    private List<String> relations;

    public ContactDetailsAdapter(Context context, List<ContactDetails> contactDetails, List<String> relations ) {
        this.context = context;
        this.contactDetails = contactDetails;
        this.relations=relations;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_widget_contact_registry, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        holder.contactQuestionText.setText(contactDetails.get(position).getQuestionText());
        holder.contactTvNumber.setText(contactDetails.get(position).getQuestionNumber());

        holder.contactID.setText(contactDetails.get(position).getContactID());
        holder.contactName.setText(contactDetails.get(position).getContactName());
        //holder.contactAge.setText(contactDetails.get(position).getContactAge());
        holder.contactGender.setText(contactDetails.get(position).getContactGender());
        holder.contactRelation.setText(contactDetails.get(position).getContactRelationships());

        holder.relationsList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, relations));

    }

    @Override
    public int getItemCount() {
        return contactDetails.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        private TextView contactQuestionText;
        public TextView contactTvNumber;

        public TextView contactID;
        public TextView contactName;
       // public TextView contactAge;
        public TextView contactGender;
        public TextView contactRelation;
        public Spinner relationsList;

        public ContactViewHolder(View itemView) {
            super(itemView);

            contactQuestionText = (TextView) itemView.findViewById(R.id.tvQuestionContact);
            contactTvNumber = (TextView) itemView.findViewById(R.id.tvNumberContact);

            contactID = (TextView) itemView.findViewById(R.id.patientID);
            contactName = (TextView) itemView.findViewById(R.id.patientName);
            //contactAge = (TextView) itemView.findViewById(R.id.tvQuestion);
            contactGender = (TextView) itemView.findViewById(R.id.contactGender);
            contactRelation = (TextView) itemView.findViewById(R.id.contactRelation);
            relationsList = (Spinner) itemView.findViewById(R.id.spRelations);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

}
