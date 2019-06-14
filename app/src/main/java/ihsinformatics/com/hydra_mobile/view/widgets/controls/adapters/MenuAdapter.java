package ihsinformatics.com.hydra_mobile.view.widgets.controls.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.FormMenu;

import java.util.List;

/**
 * Created by Owais on 11/17/2017.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private Context mContext;
    private List<FormMenu> formList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public MenuAdapter(Context mContext, List<FormMenu> formList) {
        this.mContext = mContext;
        this.formList = formList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FormMenu item = formList.get(position);
       // holder.thumbnail.setImageResource(R.drawable.form);
        holder.thumbnail.setImageResource(item.getThumbnail());
        holder.title.setText(item.getName());
        //    holder.count.setText("2 Forms");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String encounterName = null;
                if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION;
                    mContext.startActivity(new Intent(mContext, Form.class));
                    Form.setENCOUNTER_NAME(encounterName);
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_DEMOGRAPHICS)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_POST_OP_DEMOGRAPHICS;
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_SCREENING_CALL_IN)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_SCREENING_CALL_IN;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_SSI_DETECTION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_SSI_DETECTION;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_PRE_CIRCUMCISION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_PRE_CIRCUMCISION;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION2)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION2;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if (item.getName().equals(ParamNames.ENCOUNTER_TYPE_PIRANI_SCORING)) {
                    encounterName = ParamNames.ENCOUNTER_TYPE_PIRANI_SCORING;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                } else if(item.getName().equals(ParamNames.PATIENT_IMAGES)){
                    encounterName = ParamNames.PATIENT_IMAGES;
                    PatientInfoFetcher.init(encounterName, PatientInfoFetcher.REQUEST_TYPE.FETCH_IMAGE_INFO);
                    mContext.startActivity(new Intent(mContext, PatientInfoFetcher.class));
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }
}
