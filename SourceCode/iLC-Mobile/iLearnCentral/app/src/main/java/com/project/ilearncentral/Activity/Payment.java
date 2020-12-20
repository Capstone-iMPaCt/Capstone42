package com.project.ilearncentral.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.MyClass.Constants;
import com.project.ilearncentral.MyClass.PaymentsUtil;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.project.ilearncentral.databinding.ActivityPaymentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Payment extends AppCompatActivity {
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 571;

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    private ActivityPaymentBinding layoutBinding;
    private ImageButton googlePayButton;

    private FirebaseFirestore db;
    private DocumentReference ref;

    private double fee;
    private String title;
    private String subSystem;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        finish();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(
                                this, "Payment attempt cancelled",
                                Toast.LENGTH_LONG).show();
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);

        initialize();
    }

    private void initialize() {
        layoutBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        Intent intent = getIntent();
        fee = intent.getDoubleExtra("fee", 0);
        title = intent.getStringExtra("title");
        layoutBinding.paymentAmount.setText("PAY " + Utility.showPriceInPHP(fee));

        paymentsClient = PaymentsUtil.createPaymentsClient(this);

        layoutBinding.paymentGpayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(fee);
                if (!paymentDataRequestJson.isPresent()) {
                    return;
                }
                PaymentDataRequest request =
                        PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        Payment.this, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        });
    }

/**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see <a href="https://developers.google.com/pay/api/android/reference/
     * object#PaymentData">PaymentData</a>*/


    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String tokenizationType = tokenizationData.getString("type");
            final String token = tokenizationData.getString("token");

            if ("PAYMENT_GATEWAY".equals(tokenizationType) && "examplePaymentMethodToken".equals(token)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage(Constants.PAYMENT_GATEWAY_TOKENIZATION_NAME)
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            Subscription.setSubscriptionStatus(title, fee);

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

/**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     *                   WalletConstants.ERROR_CODE_* constants.
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * WalletConstants#constant-summary">Wallet Constants Library</a>*/


    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }
}
