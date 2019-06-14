package ihsinformatics.com.hydra_mobile.data.core.question.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nabil shafi on 6/13/2018.
 */

public class AddressConfiguration extends Configuration {

    private List<AddressTag> addressTags;
    private List<OpenAddressField> openAddressFields;

    public AddressConfiguration(List<OpenAddressField> openAddressFields, AddressTag... addressTags) {
        this.addressTags = Arrays.asList(addressTags);
        this.openAddressFields = openAddressFields;
    }

    public void setAddressTags(AddressTag... addressTags) {
        this.addressTags = Arrays.asList(addressTags);
    }

    public List<AddressTag> getSortedAddressTags() {
        Collections.sort(addressTags, new Comparator<AddressTag>() {
            @Override
            public int compare(AddressTag o1, AddressTag o2) {

                return o1.getOrder()-o2.getOrder();
            }
        });

        return addressTags;
    }

    public List<OpenAddressField> getSortedOpenAddressFields() {
        Collections.sort(openAddressFields, new Comparator<OpenAddressField>() {
            @Override
            public int compare(OpenAddressField o1, OpenAddressField o2) {

                return o1.getOrder()-o2.getOrder();
            }
        });

        return openAddressFields;
    }

    public void setOpenAddressFields(List<OpenAddressField> openAddressFields) {
        this.openAddressFields = openAddressFields;
    }

    public static class OpenAddressField {
        private int order;
        private String fieldName;
        private QuestionConfiguration configuration;
        private boolean isMandatory;
        private String paramName;

        public OpenAddressField(int order, String fieldName, QuestionConfiguration configuration, boolean isMandatory, String paramName) {
            this.order = order;
            this.fieldName = fieldName;
            this.configuration = configuration;
            this.isMandatory = isMandatory;
            this.paramName = paramName;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public QuestionConfiguration getConfiguration() {
            return configuration;
        }

        public void setConfiguration(QuestionConfiguration configuration) {
            this.configuration = configuration;
        }

        public boolean isMandatory() {
            return isMandatory;
        }

        public void setMandatory(boolean mandatory) {
            isMandatory = mandatory;
        }
    }

    public static class AddressTag {
        private int order;
        private String tagName;

        @Override
        public String toString() {
            return tagName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddressTag that = (AddressTag) o;

            return tagName.equals(that.tagName);
        }

        @Override
        public int hashCode() {
            return tagName.hashCode();
        }

        public AddressTag(int order, String tagName) {
            this.order = order;
            this.tagName = tagName;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

    }
}
