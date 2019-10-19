package com.ihsinformatics.dynamicformsgenerator.data;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Procedure;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nabil shafi on 6/6/2018.
 */

public class DynamicOptions {


    public static List<Option> getFromArray(Context context, int questionId, int[] opens, int[] hides, String... array) {
        List<Option> arrayOptions = new ArrayList<>();

        for(String s : array) {
            arrayOptions.add(new Option(questionId, 300, opens, hides, null/*userCredentials.getProviderUUID()*/, s, -1));
        }

        return arrayOptions;
    }
    public static List<Option> getProviderOptions(Context context, int questionId, int[] opens, int[] hides) {
        List<Option> providerOptions=new ArrayList<>();
        List<UserCredentials> userCred = DataAccess.getInstance().getUserCredentials(context);
        for(UserCredentials userCredentials : userCred) {
            providerOptions.add(new Option(questionId, 300, opens, hides, null/*userCredentials.getProviderUUID()*/, userCredentials.getUsername(), -1));
        }

        return providerOptions;
    }

    public static List<Option> getEvaluatorOptions(Context context, int questionId, int[] opens, int[] hides) {
        List<Option> providerOptions=new ArrayList<>();
        List<com.ihsinformatics.dynamicformsgenerator.data.pojos.Option> options = DataAccess.getInstance().fetchOptionsByTagName(context, ParamNames.ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE_NAME);
        for(com.ihsinformatics.dynamicformsgenerator.data.pojos.Option option : options) {
            providerOptions.add(new Option(questionId, 300, opens, hides, null, option.getValue(), -1));
        }

        return providerOptions;
    }

    public static List<Option> getProcedureOptions(Context context, int questionId, int[] opens, int[] hides) {
        List<Option> procedureOptions=new ArrayList<>();
        List<Procedure> procedureList = DataAccess.getInstance().getProcedures(context);
        for(Procedure procedure : procedureList) {
            procedureOptions.add(new Option(questionId, 300, opens, hides, procedure.getUuid(), procedure.getName(), -1));
        }

        return procedureOptions;
    }

}
