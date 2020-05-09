package com.demo.conversion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.utils.JUtil;

import java.math.BigInteger;

import static com.demo.utils.JUtil.SPACE_DELIMITER;
import static com.demo.utils.JUtil.formedIntelLine;
import static com.demo.utils.JUtil.hexByteArrToStr;
import static com.demo.utils.JUtil.twoDigitHexStrToAstrLine;

public class MainActivity extends AppCompatActivity {

    private static final int INTEL_CHECKSUM_BITS_SIZE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadListener();
    }
    private TextView mResultTv, mIntelLineTv;
    private EditText mEnterHexLineEtv, mDecStrHolderEtv, mHexStrHolderEtv, mBinStrHolderEtv, mHexTo2sComplementHolderEtv;

    private void loadListener() {
        mResultTv = findViewById(R.id.resultTv);
        mIntelLineTv = findViewById(R.id.intelLineTv);
        mEnterHexLineEtv = findViewById(R.id.enterHexLineEtv);
        mDecStrHolderEtv = findViewById(R.id.enterDecToHexEtv);
        mHexStrHolderEtv = findViewById(R.id.enterHexToDecEtv);
        mBinStrHolderEtv = findViewById(R.id.enterBinToHexDecEtv);
        mHexTo2sComplementHolderEtv = findViewById(R.id.hexToConv2sComplementEtv);

        findViewById(R.id.addHexBytesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = onValidateInputLineToHexDigits(mEnterHexLineEtv.getText().toString()) ;
                if(!TextUtils.isEmpty(inputStr)){
                    String line = twoDigitHexStrToAstrLine(inputStr);
                    String intelLine = formedIntelLine(getString(R.string.intel_format_line), line, "");
                    String lsbDigitSum = additionOfHexInputStr(twoDigitHexStrToAstrLine(line));
                    updateViewWithResult(lsbDigitSum, intelLine);
                }
            }
        });
        findViewById(R.id.convertToXSumBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = onValidateInputLineToHexDigits(mEnterHexLineEtv.getText().toString()) ;
                if(!TextUtils.isEmpty(inputStr)){
                    String line = twoDigitHexStrToAstrLine(inputStr);
                    String xSum = getCalCheckSum(additionOfHexInputStr(line));
                    String intelLine = formedIntelLine(getString(R.string.intel_format_line), line, xSum);
                    updateViewWithResult(xSum, intelLine);
                }
            }
        });
        findViewById(R.id.decToHexBinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStrDec = mDecStrHolderEtv.getText().toString();
                if (!TextUtils.isEmpty(inputStrDec)) {
                    BigInteger decimalNum = new BigInteger(twoDigitHexStrToAstrLine(inputStrDec));
                    mBinStrHolderEtv.setText(decimalNum.toString(2));
                    mHexStrHolderEtv.setText(decimalNum.toString(16));
                }
            }
        });

        findViewById(R.id.hexToDecBinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hexStr = mHexStrHolderEtv.getText().toString();
                if (!TextUtils.isEmpty(hexStr)) {
                    BigInteger decimalNum = new BigInteger(twoDigitHexStrToAstrLine(hexStr), 16);
                    mDecStrHolderEtv.setText(decimalNum.toString());
                    mBinStrHolderEtv.setText(decimalNum.toString(2));
                }
            }
        });
        findViewById(R.id.binToHexDecBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hexStr = mBinStrHolderEtv.getText().toString();
                if (!TextUtils.isEmpty(hexStr)) {
                    BigInteger decimalNum = new BigInteger(twoDigitHexStrToAstrLine(hexStr), 2);
                    mHexStrHolderEtv.setText(decimalNum.toString(16));
                    mDecStrHolderEtv.setText(decimalNum.toString());
                    String hex2sComplement = getCalCheckSum(decimalNum.toString(16));
                }
            }
        });

        findViewById(R.id.convert2sComplementBtn).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
               String digitsValue = mHexTo2sComplementHolderEtv.getText().toString();
                if (!TextUtils.isEmpty(digitsValue)) {
                    mHexTo2sComplementHolderEtv.setText(getCalCheckSum(digitsValue));
                    mHexTo2sComplementHolderEtv.setTextColor(R.color.red);
                }
            }
        });
    }

    private void updateViewWithResult(final String result, final String intelLine ) {
       this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultTv.setText(result);
                mIntelLineTv.setText(intelLine);
            }
        });
    }

    private String getCalCheckSum(String lsbHexLine) {
        String resultAsCheckSum = "";
        if(!TextUtils.isEmpty(lsbHexLine)) {
            int intelCheckSumBits = INTEL_CHECKSUM_BITS_SIZE; //lsbLine.length();
            long value = Long.parseLong(lsbHexLine, 16);
            long resultAs2sComplement = (~value) + 1;
             resultAsCheckSum = Long.toHexString(resultAs2sComplement);
            int newLength = resultAsCheckSum.length();
            if (newLength > intelCheckSumBits) {
                resultAsCheckSum = resultAsCheckSum.substring(newLength - intelCheckSumBits);
            }
        }
//        Log.e(TAG, lsbHexLine + " JAVA LSB(String)  = "+resultAsCheckSum );
        return resultAsCheckSum;
    }

    private String additionOfHexInputStr(String intelLine) {
        String lsbHexLine = "";
        if(!TextUtils.isEmpty(intelLine)) {
            int valueInt = 0;
            for (int i = 0; i < intelLine.length(); i += 2) {
                int decimal = Integer.parseInt(intelLine.substring(i, i + 2), 16);
//            Log.e(TAG, valueInt + " Hex To Decimal =  "+ decimal );
                valueInt += decimal;
            }
             lsbHexLine = Integer.toHexString(valueInt);//  String hex = "FF30";
        }
        return lsbHexLine;
    }

    private String onValidateInputLineToHexDigits(String enteredValue) {
        String result = "";
        if(!TextUtils.isEmpty(enteredValue)){
           String twoDigitsHex = JUtil.aStringToTwoDigitHexStr(enteredValue);
            mEnterHexLineEtv.setText(twoDigitsHex);
            result = twoDigitsHex;
        }
        return result;
    }


    /*private class CurrValueEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String editedValue = charSequence.toString().trim();
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String enteredValue = editable.toString();

        }
    }*/

}
