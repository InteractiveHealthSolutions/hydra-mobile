package com.ihsinformatics.dynamicformsgenerator.utils;


public class Regex {
	
	/** Integer value determining the length of Identifer scheme used */
	public static final int CHILD_PROGRAMID_LENGTH = 14;
	
	/** Special Case of child ID */
	public static final Regex CHILD_ID = new Regex( "[1-5].{"+(CHILD_PROGRAMID_LENGTH-1)+"}");

	/** Indus HMIS MRNO*/
	public static final Regex MRNO = new Regex("\\d{11}");

	/** SSI ID*/
	public static final Regex SSID = new Regex("\\d{3}");

	/**
	 * No of Rooms in a house. Can be from 1 - 15
	 */
	public static final Regex NO_OF_ROOMS = new Regex("[1-9]|[1][0-5]{1,2}");

	/***
	 * 2 digit Non-Zero
	 */
	public static final Regex NON_ZERO_2_DIGIT= new Regex("[1-9][0-9]{1,2}");

	/** The Constant ALPHA. */
	public static final Regex ALPHA = new Regex("[a-zA-Z]+");
	
	/** The Constant ALPHA_NUMERIC. */
	public static final Regex ALPHA_NUMERIC = new Regex("[a-zA-Z0-9]{5,20}+");
	
	/** The Constant CELL_NUMBER. */
	public static final Regex CELL_NUMBER = new Regex("(\\+92|92|0)3[0-9]{9}");

	/** The Constant MOBILE_NUMBER. */
	public static final Regex MOBILE_NUMBER = new Regex("(03)([0-9]{2})([-])([0-9]{7})");
	
	/** The Constant EMAIL. */
	public static final Regex EMAIL = new Regex("((\\w+)|(\\w+\\.+\\w+)|(\\w+\\-+\\w+))\\@((\\w+)|(\\w+\\-\\w+))\\.\\w{2,4}");
	
	/** The Constant NAME_CHARACTERS. */
	public static final Regex NAME_CHARACTERS = new Regex("[a-zA-Z\\.\\-/\\s]+");
	
	/** The Constant NO_SPECIAL_CHAR. */
	public static final Regex NO_SPECIAL_CHAR = new Regex("[a-zA-Z0-9\\|/\\(\\)_,\\-\\.\\s`]+");
	
	/** The Constant NUMERIC. */
	public static final Regex NUMERIC = new Regex("[0-9]+");

	/** Decimal Number */
	public static final Regex DECIMAL_NUMBER = new Regex("[0-9]+(\\.[0-9]+)?");

	/** The Constant PASSWORD. */
	public static final Regex PASSWORD = new Regex("[a-zA-Z0-9@!\\|~/\\$\\*\\(\\)_\\[\\]\\{\\};:\\.\\s]{6,20}+");
	
	/** The Constant PHONE_NUMBER. */
	public static final Regex PHONE_NUMBER = new Regex("(\\d+([,\\-\\s]?)\\d+)+");
	
	/** The Constant PTCL_LANDLINE_NUMBER. */
	public static final Regex PTCL_LANDLINE_NUMBER = new Regex("(\\+92|92|0)?213[0-9]{7}");
	
	/** The Constant PTCL_WIRELESS_NUMBER. */
	public static final Regex PTCL_WIRELESS_NUMBER = new Regex("(\\+92|92|0)?213[0-9]{7}");
	
	/** The Constant WHITESPACE_WORD. */
	public static final Regex WHITESPACE_WORD = new Regex("[\\w\\s]+");
	
	/** The Constant WHITESPACE_ALPHA. */
	public static final Regex WHITESPACE_ALPHA = new Regex("[a-zA-Z\\s]+");
	
	/** The Constant WHITESPACE_NUMERIC. */
	public static final Regex WHITESPACE_NUMERIC = new Regex("[0-9\\s]+");
	
	/** The Constant WHITESPACE_ALPHA_NUMERIC. */
	public static final Regex WHITESPACE_ALPHA_NUMERIC = new Regex("[a-zA-Z0-9\\s]+");
	
	/** The Constant WORD. */
	public static final Regex WORD = new Regex("\\w+");

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
		   return regexp;
		}
		
		/** The regexp. */
		private final String regexp;

		/**
		 * Instantiates a new rE g_ ex.
		 *
		 * @param exp the exp
		 */
		private Regex(String exp) {
			regexp=exp;
		}

}
