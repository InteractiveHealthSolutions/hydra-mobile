package ihsinformatics.com.hydra_mobile.utils;

import java.util.HashMap;
import java.util.Iterator;

public class Translator {
	
	private HashMap<String, TranslatedText> translatedUrduText;
	
	public static enum LANGUAGE {
		ENGLISH,
		URDU
	}
	
	private static Translator instance;
	
	public static Translator getInstance() {
		if(instance == null) {
			instance = new Translator();
		}
		
		return instance;
	}
	
	public String Translate(String text, LANGUAGE language) {
		switch (language) {
		case URDU:
			if(translatedUrduText.containsKey(text)) {
				return translatedUrduText.get(text).getText();
			}
			
			break;
		case ENGLISH:
			
			Iterator<String> it = translatedUrduText.keySet().iterator();
			while(it.hasNext()) {
				String s = it.next();
				TranslatedText tt = translatedUrduText.get(s);
				if(tt.translatedText.equals(text)) {
					return s;
				}
			}
			break;
		
		default:
			break;
		}
		
		return text;
	}
	
	public class TranslatedText {
		private String englishText;
		private String translatedText;
		private LANGUAGE language;
		
		public TranslatedText(String text, LANGUAGE language) {
			super();
			this.translatedText = text;
			this.language = language;
		}

		public String getText() {
			return translatedText;
		}
		
		public void setText(String text) {
			this.translatedText = text;
		}

		public LANGUAGE getLanguage() {
			return language;
		}

		public void setLanguage(LANGUAGE language) {
			this.language = language;
		}

		public String getEnglishText() {
			return englishText;
		}

		public void setEnglishText(String englishText) {
			this.englishText = englishText;
		}

		public String getTranslatedText() {
			return translatedText;
		}

		public void setTranslatedText(String translatedText) {
			this.translatedText = translatedText;
		}
		
	}

	private Translator() {
		translatedUrduText = new HashMap<String, TranslatedText>();
		
		translatedUrduText.put("Date", new TranslatedText("Tareekh", LANGUAGE.URDU));
		translatedUrduText.put("Yes", new TranslatedText("Haan", LANGUAGE.URDU));
		translatedUrduText.put("No", new TranslatedText("Nahi", LANGUAGE.URDU));
        translatedUrduText.put("Other", new TranslatedText("Deegar", LANGUAGE.URDU));
        translatedUrduText.put("other", new TranslatedText("Deegar", LANGUAGE.URDU));
        translatedUrduText.put("Other (open text)", new TranslatedText("Deegar", LANGUAGE.URDU));
		translatedUrduText.put("Don't Know", new TranslatedText("Pata nahi", LANGUAGE.URDU));
        translatedUrduText.put("Refused", new TranslatedText("Inkar krdya", LANGUAGE.URDU));
        translatedUrduText.put("Not applicable", new TranslatedText("Naqabil-e-itlaaq", LANGUAGE.URDU));
        translatedUrduText.put("None", new TranslatedText("Koi nahi", LANGUAGE.URDU));
		translatedUrduText.put("Yes", new TranslatedText("Haan", LANGUAGE.URDU));
		translatedUrduText.put("No", new TranslatedText("Nahi", LANGUAGE.URDU));
		translatedUrduText.put("Patient's name", new TranslatedText("Mareez ka naam", LANGUAGE.URDU));
		translatedUrduText.put("Father's or Husband's name", new TranslatedText("Walid ya shohar ka naam", LANGUAGE.URDU));
		translatedUrduText.put("Age", new TranslatedText("Umar", LANGUAGE.URDU));
		translatedUrduText.put("Age unit", new TranslatedText("Umar (din, haftey, mahiney ya saal)", LANGUAGE.URDU));
		translatedUrduText.put("Date of birth", new TranslatedText("Date of birth", LANGUAGE.URDU));
		translatedUrduText.put("Gender", new TranslatedText("Jins (Gender)", LANGUAGE.URDU));
		translatedUrduText.put("Marital Status", new TranslatedText("Azdawaji hasiyat (Marital status)", LANGUAGE.URDU));
		translatedUrduText.put("House/Street + Colony/Town", new TranslatedText("Ghar ka patta - Ghar/Street #", LANGUAGE.URDU));
		translatedUrduText.put("Colony/Town", new TranslatedText("Colony/Town", LANGUAGE.URDU));
		translatedUrduText.put("Area", new TranslatedText("Ilaqa (Area)", LANGUAGE.URDU));
		translatedUrduText.put("City / Village", new TranslatedText("Sheher / Gaaon", LANGUAGE.URDU));
		translatedUrduText.put("Rural / Urban", new TranslatedText("Dehati / Shehri", LANGUAGE.URDU));
		translatedUrduText.put("Contact Number 1", new TranslatedText("Rabta number 1", LANGUAGE.URDU));
		translatedUrduText.put("Patient relationship to contact", new TranslatedText("Kiss ka number hai?", LANGUAGE.URDU));
		translatedUrduText.put("Contact Number 2", new TranslatedText("Rabta number 2", LANGUAGE.URDU));
        translatedUrduText.put("Male", new TranslatedText("Mard", LANGUAGE.URDU));
        translatedUrduText.put("Female", new TranslatedText("Aurat", LANGUAGE.URDU));

/*		translatedUrduText.put("SCREENER’S INITIALS", new TranslatedText("screener_initial", LANGUAGE.URDU));
		translatedUrduText.put("Today's date", new TranslatedText("Aaj ki tarekh", LANGUAGE.URDU));
		translatedUrduText.put("Study identifier", new TranslatedText("Shareek ki ID", LANGUAGE.URDU));
		translatedUrduText.put("Participant’s Father/Husband’s Name", new TranslatedText("Walid/shohar ka naam?", LANGUAGE.URDU));
		translatedUrduText.put("PARTICIPANT’S GIVEN NAME", new TranslatedText("naam?", LANGUAGE.URDU));
		translatedUrduText.put("Participant’s telephone number", new TranslatedText("shareek ka mobile number", LANGUAGE.URDU));
		translatedUrduText.put("Participant's date of birth", new TranslatedText("shareek ki tarekh-e-paidaish", LANGUAGE.URDU));
		translatedUrduText.put("Are you taking any of the following drugs: \n" +
				"* Myrin-P, or \n" +
				"* Rimstar Forte;\n" +
				" or are you taking a medicine that makes your urine a dark orange colour?", new TranslatedText("Kia app ise koi dawa lay rahay hien jiss say aap ka peshab pela yeah zard rang ka ho raha hoo?", LANGUAGE.URDU));
		translatedUrduText.put("Are you currently being treated with medication for TB?", new TranslatedText("Kia aap aaj kal TB ka illaj karwa rahien hain?", LANGUAGE.URDU));
		translatedUrduText.put("Have you ever been treated for TB in the past?", new TranslatedText("Kia aap ka phelay bhi TB ka elaj hoa hay?", LANGUAGE.URDU));
		translatedUrduText.put("(If yes,) how many years ago was that?", new TranslatedText("kitnay saal phelay hoa tha?", LANGUAGE.URDU));
		translatedUrduText.put("Select each symptom that the participant has", new TranslatedText("Jawab denay walay ko konsi alamat hien", LANGUAGE.URDU));
		translatedUrduText.put("Cough?", new TranslatedText("Khansi?", LANGUAGE.URDU));
		translatedUrduText.put("How many days have you had this cough?", new TranslatedText("Kitnay dino say ye khansi hay?", LANGUAGE.URDU));
		translatedUrduText.put("If not coughing TODAY, in the past month have you been coughing?", new TranslatedText("Agar haal mien khansi naheen hoi hay, liken kia maazi mien Kabhee aap ko khansee ki sheekayat hoi thee?", LANGUAGE.URDU));
		translatedUrduText.put("Weight loss?", new TranslatedText("wazan ka ghatna?", LANGUAGE.URDU));
		translatedUrduText.put("Shortness of breath?", new TranslatedText("hapna", LANGUAGE.URDU));
		translatedUrduText.put("Night sweats?", new TranslatedText("raat ko sotay hoay paseena aana?", LANGUAGE.URDU));
		translatedUrduText.put("Hemoptysis (any time in the last 6 months)?", new TranslatedText("pechlay 6 maheeno mien kabhe bhee khoon tokhna ya khoon mili bulghum thookhna?", LANGUAGE.URDU));
		translatedUrduText.put("Sputum (phlegm)", new TranslatedText("bulgham ka ana", LANGUAGE.URDU));
		translatedUrduText.put("Fever?", new TranslatedText("bukhar?", LANGUAGE.URDU));
		translatedUrduText.put("Chest pain?", new TranslatedText("seenay mien dard", LANGUAGE.URDU));
		translatedUrduText.put("Has someone in the house been diagnosed or treated for TB in the past 2 years?", new TranslatedText("Kia pechlay 2 salaon mien aap kay gharanay kay kise fard ko tb hoa hay?", LANGUAGE.URDU));
		translatedUrduText.put("Has a family member, close friend, or colleague that you spend more than 3 hours per week with, been diagnosed or treated for TB in the past 2 years?", new TranslatedText("Kia pechlay 2 saal mien aap kay koi rishtaydar ya kareebi doost yeah rafique jin kay sath aap haftah mien 3 ghantay say ziada guzartay hien - in ko tb hoa hay", LANGUAGE.URDU));
		translatedUrduText.put("Consent form signed?", new TranslatedText("ijazat namay per nishan dal dia hai?", LANGUAGE.URDU));
		translatedUrduText.put("MR Number", new TranslatedText("MR NUMBER", LANGUAGE.URDU));
		translatedUrduText.put("NAME OF CONTACT", new TranslatedText("koi rabtay ka zaria yeah naam jo aap say baat karwa sakay", LANGUAGE.URDU));
		translatedUrduText.put("PHONE NUMBER", new TranslatedText("zarya ka mobile number", LANGUAGE.URDU));
		translatedUrduText.put("Interview Start time", new TranslatedText("interview shuru karnay ka waqt", LANGUAGE.URDU));
		translatedUrduText.put("Participant’s gender", new TranslatedText("shareek ka jins", LANGUAGE.URDU));
		translatedUrduText.put("Do you have any of the following medical conditions?", new TranslatedText("kia aap ko in mien say koi bemari ya takleef hien", LANGUAGE.URDU));
		translatedUrduText.put("Diabetes?", new TranslatedText("ziabitees ya sugar ki bemari", LANGUAGE.URDU));
        translatedUrduText.put("HIV/AIDS?", new TranslatedText("hiv ya aids ki bemari", LANGUAGE.URDU));
		translatedUrduText.put("Asthma?", new TranslatedText("dama?", LANGUAGE.URDU));
		translatedUrduText.put("Bronchitis?", new TranslatedText("halaq ya pheparoon ki soojan bronchitis?", LANGUAGE.URDU));
		translatedUrduText.put("Any other condition? Please specify", new TranslatedText("koi degar bemari?", LANGUAGE.URDU));
		translatedUrduText.put("Do you smoke tobacco?", new TranslatedText("Kia aap tambako noshi kartay hien?", LANGUAGE.URDU));
		translatedUrduText.put("How old were you when you started smoking?", new TranslatedText("Aap nay kis umer say tambako noshi shoroh ki?", LANGUAGE.URDU));
		translatedUrduText.put("How do you smoke tobacco?", new TranslatedText("Aap din mien kitni baar tambako noshi kartay hien", LANGUAGE.URDU));
		translatedUrduText.put("Cigarettes per day?", new TranslatedText("Cigarettes din mien kitni baar?", LANGUAGE.URDU));
		translatedUrduText.put("Beedi per day?", new TranslatedText("Beedi  din mien kitni baar?", LANGUAGE.URDU));
		translatedUrduText.put("Pipe per day?", new TranslatedText("Pipe  din mien kitni baar?", LANGUAGE.URDU));
		translatedUrduText.put("Cigar per day?", new TranslatedText("Cigar  din mien kitni baar?", LANGUAGE.URDU));
		translatedUrduText.put("Hookah per day?", new TranslatedText("Hookah din mien kitni baar?", LANGUAGE.URDU));
		translatedUrduText.put("Do you take any other form of tobacco?", new TranslatedText("App kisi aur kisam ti tambaco noshee kertay hien?", LANGUAGE.URDU));
		translatedUrduText.put("Any other form of tobacco? Specify type", new TranslatedText("Koi degar kisam ki tambako nooshi?", LANGUAGE.URDU));
		translatedUrduText.put("How many times do you smoke tobacco?", new TranslatedText("Aap din mien kitni baar tambako noshi kartay hien", LANGUAGE.URDU));
		translatedUrduText.put("What is your current occupation?", new TranslatedText("App ka is waqat pesha kia hay?", LANGUAGE.URDU));
		translatedUrduText.put("How many years have you held that occupation?", new TranslatedText("App is paishay mien kitnay saal say hien?", LANGUAGE.URDU));
		translatedUrduText.put("Did you have any other occupation prior to this one?", new TranslatedText("Kia aap is say phelay koi aur kaam kartay thay?", LANGUAGE.URDU));
		translatedUrduText.put("If yes, please list the last 3 occupations prior to the current one.", new TranslatedText("agar haan to pechlay teen paishaon kay baray mien bataien", LANGUAGE.URDU));
		translatedUrduText.put("(Prior occupation 1) What occupation was it?", new TranslatedText("1. Kia paisha tha?", LANGUAGE.URDU));
		translatedUrduText.put("How many years did you hold that occupation?", new TranslatedText("App is paishay mien kitnay saal thay?", LANGUAGE.URDU));
		translatedUrduText.put("(Prior occupation 2) What occupation was it?", new TranslatedText("2. Kia pisha tha?", LANGUAGE.URDU));
		translatedUrduText.put("How many years did you hold that occupation?", new TranslatedText("App is paishay mien kitnay saal thay?", LANGUAGE.URDU));
		translatedUrduText.put("(Prior occupation 3) What occupation was it?", new TranslatedText("3. Kia pisha tha?", LANGUAGE.URDU));
		translatedUrduText.put("How many years did you hold that occupation?", new TranslatedText("App is paishay mien kitnay saal thay?", LANGUAGE.URDU));
		translatedUrduText.put("What kind of cooking fuel do you use at home?", new TranslatedText("Aap kay ghar kay cholay mien kahna pakanay kay liay kia istamal hota hay?", LANGUAGE.URDU));
		translatedUrduText.put("Specify other type of cooking fuel used at home", new TranslatedText("Koi degar kisam", LANGUAGE.URDU));
		translatedUrduText.put("Measure participant's HEIGHT in cm", new TranslatedText("shareek ka kad maloom karien", LANGUAGE.URDU));
        translatedUrduText.put("Measure participant's WEIGHT in kg", new TranslatedText("shareek ka wazan", LANGUAGE.URDU));
        translatedUrduText.put("Measure and record FINGER-STICK GLUCOSE", new TranslatedText("sugar test ka nateja", LANGUAGE.URDU));
        translatedUrduText.put("METHOD", new TranslatedText("METHOD", LANGUAGE.URDU));
        translatedUrduText.put("TIME Collected", new TranslatedText("TIME Collected", LANGUAGE.URDU));
        translatedUrduText.put("Date collected", new TranslatedText("Date collected", LANGUAGE.URDU));
        translatedUrduText.put("Induction successful?", new TranslatedText("Induction successful?", LANGUAGE.URDU));
        translatedUrduText.put("Adverse Event?", new TranslatedText("Adverse Event?", LANGUAGE.URDU));

		//Pre-op Demographics Questions
		translatedUrduText.put("Patient relationship to contact 2", new TranslatedText("Kiss ka number hai?", LANGUAGE.URDU));
		translatedUrduText.put("Have you ever attended school", new TranslatedText("Kya aapney kabhi school mein shirkat ki hai?", LANGUAGE.URDU));
		translatedUrduText.put("Number of years of formal schooling", new TranslatedText("Aap ne school main kitnay saal mukamal kiye hain?", LANGUAGE.URDU));
		translatedUrduText.put("Any informal education", new TranslatedText("Koi ghair rasmi taleem hasil ki hai?", LANGUAGE.URDU));
		translatedUrduText.put("Can you read/write well enough to complete SSI screening questionnaire?", new TranslatedText("Kya aap parh likh saktey hai aur SSI screening questionnaire (jo aap ko diya jai ga) kudh bhar sakein gay?", LANGUAGE.URDU));
		translatedUrduText.put("If no, or if you are a pediatric patient, who will assist you in answering or translating the SSI screening questions?", new TranslatedText("Agar nahi, ya agar aap pediatric mareez hai, to appko SSI screening sawalname ka jawab deney mein ya us ka tarjuma karney mein kaun madad kareyga?", LANGUAGE.URDU));
		translatedUrduText.put("Assistant helper's number of years of formal schooling", new TranslatedText("Madad kerney waley ney kitney saal rasmi taleem hasil ki?", LANGUAGE.URDU));
		translatedUrduText.put("Patient's primary language", new TranslatedText("Aap kiss zaban se taaluq rakhtey hain?", LANGUAGE.URDU));
		translatedUrduText.put("Does any member of your household own a bank account?", new TranslatedText("Kya aap ke ghar main kisi ka bank account hai?", LANGUAGE.URDU));
		translatedUrduText.put("Does your household have a chair/bench?", new TranslatedText("Kya aapke ghar main bethney ke liye kursi ya sofa hai?", LANGUAGE.URDU));
		translatedUrduText.put("Does your household have a mattress?", new TranslatedText("Kya aapke ghar main soney ke liye gadda hai ?", LANGUAGE.URDU));
		translatedUrduText.put("Does your household have a refrigerator?", new TranslatedText("Kya aap key ghar mein fridge hai?", LANGUAGE.URDU));
		translatedUrduText.put("Does your household have a table?", new TranslatedText("Kya aap key ghar mein maiz hai?", LANGUAGE.URDU));
		translatedUrduText.put("Does your household have a television?", new TranslatedText("Kya aap key ghar mein TV hai?", LANGUAGE.URDU));
		translatedUrduText.put("Do you have a separate room which is used as a kitchen?", new TranslatedText("Kya aapke ghar main alaida karma bawarchi khaney ke tor par istimal hotahai?", LANGUAGE.URDU));
		translatedUrduText.put("What is the average monthly income for the entire household?", new TranslatedText("Aapke gharaney ki austan mahanaa amdani kitni hai?", LANGUAGE.URDU));
		translatedUrduText.put("How many rooms are there in your house?", new TranslatedText("Aap key ghar mein kitney kamrey hai?", LANGUAGE.URDU));
		translatedUrduText.put("How many people usually sleep in this household?", new TranslatedText("Aam tor par aapke ghar main kitne log rehtey hain?", LANGUAGE.URDU));
		translatedUrduText.put("What kind of toilet facility do members of your household usually use?", new TranslatedText("Aapke ghar main toilet ki kon si saholatain mojood hain?", LANGUAGE.URDU));
		translatedUrduText.put("What kind of toilet facility do members of your household usually use? Other", new TranslatedText("Aapke ghar main toilet ki kon se saholatain mojood hain? Degar", LANGUAGE.URDU));
		translatedUrduText.put("What is the main source of drinking water for members of your household?", new TranslatedText("Aapke ghar main peeney ke pani kahan se aata hai?", LANGUAGE.URDU));
		translatedUrduText.put("What is the main source of drinking water for members of your household? Other", new TranslatedText("Aapke ghar main peeney ke pani kahan se aata hai? Degar", LANGUAGE.URDU));
		translatedUrduText.put("Do you own a mobile phone?", new TranslatedText("Kya aap key pass apna mobile phone hai?", LANGUAGE.URDU));
		translatedUrduText.put("If no, do you have regular/constant access to a mobile phone?", new TranslatedText("Agar nahi hai, to kya aapkey pass aisa koi phone hai jis per aap sey asani sey musalsal rabta ho sakta hai?", LANGUAGE.URDU));
		translatedUrduText.put("Do you use your mobile phone to send and/or receive text messages?", new TranslatedText("Kya aap phone sey asani se sms ker saktey hai?", LANGUAGE.URDU));
		translatedUrduText.put("Do you have a camera on your phone?", new TranslatedText("Kya aapke phone mein camera hai?", LANGUAGE.URDU));
		translatedUrduText.put("How long have you been using mobile phones for?", new TranslatedText("Aap kitney arsey sey mobile phones istamal kar rahey hai?", LANGUAGE.URDU));
		translatedUrduText.put("Unit of duration?", new TranslatedText("Saal, mahiney ya haftey. Wazahat kaarein.", LANGUAGE.URDU));
		translatedUrduText.put("Do you use internet on your mobile phone?", new TranslatedText("Kya aap mobile phone per internet istamal kartey hai?", LANGUAGE.URDU));

		//Screening Call In Questtions
		translatedUrduText.put("Confirm if the patient is calling because they think they have an infection", new TranslatedText("Tasdeek karey key mareez key call kerney ki waja yeh hai key un ko laga key un ko infection hai?", LANGUAGE.URDU));
		translatedUrduText.put("Other reason for calling", new TranslatedText("Agar koi aur wajah hai tou wazey karein", LANGUAGE.URDU));
		translatedUrduText.put("Presence of colored drainage (yellow, green or white) at the incision site", new TranslatedText("Operation ki jaga per pus (peeli, hari ya safaid) hona", LANGUAGE.URDU));
		translatedUrduText.put("Increasing swelling at the incision site", new TranslatedText("Operation ki jaga per soojan hai", LANGUAGE.URDU));
		translatedUrduText.put("Fever > 101�F/38�C since your surgery", new TranslatedText("Surgery ke baad Bukhar jo101�F/38�C sey zyada hai", LANGUAGE.URDU));
		translatedUrduText.put("Since (no.of days)", new TranslatedText("Agar haan, tou kitney dinoon sey?", LANGUAGE.URDU));
		translatedUrduText.put("Edges of the incision separating", new TranslatedText("Operation ki jaga key kinarey alag hona", LANGUAGE.URDU));
		translatedUrduText.put("Increasing redness at the incision site", new TranslatedText("Operation ki jaga mein bharti hue lali", LANGUAGE.URDU));
		translatedUrduText.put("Increasing warmth/heat from the incision", new TranslatedText("Operation ki jaga mein bharti hue garmahat", LANGUAGE.URDU));
		translatedUrduText.put("Increasing pain at the incision site compared to at the end of surgery", new TranslatedText("Operation ki jaga mein bharta hua dard surgery key iktitam key muqabaley mein ", LANGUAGE.URDU));
		translatedUrduText.put("Does the patient think they have an infection?", new TranslatedText("Kya mareez ko lagta hai key un to infection hai?", LANGUAGE.URDU));
		translatedUrduText.put("If yes, since how many days", new TranslatedText("Agar haan, tou kitney dinoon sey?", LANGUAGE.URDU));
		translatedUrduText.put("No signs or symptons, yet the patient thinks he has an infection", new TranslatedText("Koi alamaat nahi hai per mareez ko phir bhi lagta hai key unko infection hai?", LANGUAGE.URDU));


		//Post-op Follow-up Questtions
		translatedUrduText.put("Follow up status", new TranslatedText("Follow up hasiyat (status)", LANGUAGE.URDU));
		translatedUrduText.put("Date of follow up", new TranslatedText("Follow up visit ki tareeq", LANGUAGE.URDU));
		translatedUrduText.put("Have you followed up with any other healthcare provider (besides IH) since your surgery about this particular surgical complaint?", new TranslatedText("Kya app surgery key baad, surgery ki takleef ki waja sey, indus hospital key ilawa, kisi aur dekhbhaal feraham kerney waley key pass gai hai?           ", LANGUAGE.URDU));
		translatedUrduText.put("If yes, who did you go to for treatment?", new TranslatedText("Agar haan tou kiss ke pass gaye they, wazahat karey", LANGUAGE.URDU));
		translatedUrduText.put("If yes, specify why", new TranslatedText("Kiss wajah se, wazahar karey", LANGUAGE.URDU));
		translatedUrduText.put("If yes, what treatment did they provide you?", new TranslatedText("Agar haan, to unho nein aapko kya ilaaj feraham kiya?", LANGUAGE.URDU));
		translatedUrduText.put("Other treatment", new TranslatedText("Koi aur ilaaj, wazay karey", LANGUAGE.URDU));
		translatedUrduText.put("Do you as the HCW feel like the patient has an infection?", new TranslatedText("Kya aapko lagta hai ke mareez ko infection hai?", LANGUAGE.URDU));

		//All answers as options
		translatedUrduText.put("Days", new TranslatedText("Din", LANGUAGE.URDU));
		translatedUrduText.put("Weeks", new TranslatedText("Haftey", LANGUAGE.URDU));
		translatedUrduText.put("Months", new TranslatedText("Mahiney", LANGUAGE.URDU));
		translatedUrduText.put("Years", new TranslatedText("Saal", LANGUAGE.URDU));
		translatedUrduText.put("Single", new TranslatedText("Ghair shaadi shuda", LANGUAGE.URDU));
		translatedUrduText.put("Married", new TranslatedText("Shaadi shuda", LANGUAGE.URDU));
		translatedUrduText.put("Widowed", new TranslatedText("Bewa", LANGUAGE.URDU));
		translatedUrduText.put("Divorced", new TranslatedText("Talaaq yafta", LANGUAGE.URDU));
		translatedUrduText.put("Separated", new TranslatedText("Alaidgi", LANGUAGE.URDU));
		translatedUrduText.put("Rural", new TranslatedText("Dehati", LANGUAGE.URDU));
		translatedUrduText.put("Urban", new TranslatedText("Shehri", LANGUAGE.URDU));
		translatedUrduText.put("Self", new TranslatedText("Khud", LANGUAGE.URDU));
		translatedUrduText.put("Relative", new TranslatedText("Rishtedar", LANGUAGE.URDU));
		translatedUrduText.put("Neighbour", new TranslatedText("Parosi", LANGUAGE.URDU));
		translatedUrduText.put("Friend", new TranslatedText("Dost", LANGUAGE.URDU));
		translatedUrduText.put("Madrassa", new TranslatedText("Madrassa", LANGUAGE.URDU));
		translatedUrduText.put("Adult literacy", new TranslatedText("Adult literacy", LANGUAGE.URDU));
		translatedUrduText.put("Home schooling", new TranslatedText("Ghar par taleem hasil ki", LANGUAGE.URDU));
		translatedUrduText.put("Self learnt (reading)", new TranslatedText("Khud se parhna seekha", LANGUAGE.URDU));
		translatedUrduText.put("Self learnt (reading and writing)", new TranslatedText("Khud se parhna aur likhna seekha", LANGUAGE.URDU));
		translatedUrduText.put("Parent", new TranslatedText("Waledein", LANGUAGE.URDU));
		translatedUrduText.put("No facility/bush/field or bucket toilet", new TranslatedText("Koi sahulat nahi/jhariyo mein ya ghaas phoos mein ja ker kertey hai/khulay maidan mein ya Balti istamal kertey hai", LANGUAGE.URDU));
		translatedUrduText.put("Pit latrine without fluch", new TranslatedText("Gharay wala Toilet flush ke bagair", LANGUAGE.URDU));
		translatedUrduText.put("Flush to piped sewer system", new TranslatedText("Flush jo naali sai jura hai", LANGUAGE.URDU));
		translatedUrduText.put("Flush to septic tank", new TranslatedText("Flush jo septic se jura hai", LANGUAGE.URDU));
		translatedUrduText.put("Flush to pit latrine", new TranslatedText("Flush jo gharay se jura jai", LANGUAGE.URDU));
		translatedUrduText.put("Flush to somewhere else", new TranslatedText("Flush jo kisi dosri jaga jata hai", LANGUAGE.URDU));
		translatedUrduText.put("Piped into dwelling", new TranslatedText("Pipe ke zarye pani ghar tak aata hai", LANGUAGE.URDU));
		translatedUrduText.put("Piped to yard/plot", new TranslatedText("Pipe ke zarye paani plot tak aata hai", LANGUAGE.URDU));
		translatedUrduText.put("Public tap/standpipe", new TranslatedText("Pipe ke zarya pani ilaaqai ke nal tak aata hai", LANGUAGE.URDU));
		translatedUrduText.put("Tube well or borehole", new TranslatedText("Boring ya kunwaan ka pani", LANGUAGE.URDU));
		translatedUrduText.put("Protected well", new TranslatedText("Hifazati shuda kunwan", LANGUAGE.URDU));
		translatedUrduText.put("Unprotected well", new TranslatedText("Gher hifazati shuda kuwan", LANGUAGE.URDU));
		translatedUrduText.put("Surface water (river/dam/lake/pond/stream/canal/irrigation canal)", new TranslatedText("Darya/dam/jheel/talab/stream/nehar", LANGUAGE.URDU));
		translatedUrduText.put("Wound cleaning", new TranslatedText("Zakham ki safai", LANGUAGE.URDU));
		translatedUrduText.put("Antibiotics", new TranslatedText("Antibiotics", LANGUAGE.URDU));
		translatedUrduText.put("Dressing", new TranslatedText("Dressing", LANGUAGE.URDU));*/
	
	
	}
}
