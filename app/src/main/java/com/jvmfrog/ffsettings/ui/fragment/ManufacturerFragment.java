package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jvmfrog.ffsettings.adapter.ManufacturersAdapter;
import com.jvmfrog.ffsettings.ui.dialog.ChangeUsernameDialog;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentManufacturerBinding;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import games.moisoni.google_iab.BillingConnector;
import games.moisoni.google_iab.BillingEventListener;
import games.moisoni.google_iab.enums.ProductType;
import games.moisoni.google_iab.enums.PurchasedResult;
import games.moisoni.google_iab.enums.SupportState;
import games.moisoni.google_iab.models.BillingResponse;
import games.moisoni.google_iab.models.ProductInfo;
import games.moisoni.google_iab.models.PurchaseInfo;

public class ManufacturerFragment extends Fragment {

    private FragmentManufacturerBinding binding;

    private BillingConnector billingConnector;
    private final List<PurchaseInfo> purchasedInfoList = new ArrayList<>();
    private final List<ProductInfo> fetchedProductInfoList = new ArrayList<>();
    private ArrayList<String> arrayList;

    private boolean userPrefersAdFree = false;

    private final String LICENSE_KEY = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManufacturerBinding.inflate(inflater, container, false);

        if (SharedPreferencesUtils.getString(getActivity(), "user_name") == null || SharedPreferencesUtils.getString(getActivity(), "user_name").equals("")) {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + getString(R.string.user_name) + "!");
        } else {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + SharedPreferencesUtils.getString(getActivity(), "user_name") + "!");
        }

        if (SharedPreferencesUtils.getBoolean(getActivity(), "isAdFree")) {
            binding.buySubscriptionBtn.setVisibility(View.GONE);
        }

        arrayList = new ArrayList<>();

        if (!SharedPreferencesUtils.getBoolean(getActivity(), "isFakeManufacturerNames")) {
            arrayList.add("Samsung");
            arrayList.add("iPhone");
            arrayList.add("Xiaomi");
            arrayList.add("Redmi");
            arrayList.add("Oppo");
            arrayList.add("Huawei");
            arrayList.add("Poco");
            arrayList.add("Honor");
            arrayList.add("LG");
            arrayList.add("ZTE");
            arrayList.add("Vivo");
            arrayList.add("Motorola");
            arrayList.add("Realme");
            arrayList.add("OnePlus");
        } else {
            arrayList.add("Sumsang");
            arrayList.add("iRhone");
            arrayList.add("Xioami");
            arrayList.add("Rebmi");
            arrayList.add("Oddo");
            arrayList.add("Huowei");
            arrayList.add("Poso");
            arrayList.add("Hohor");
            arrayList.add("GL");
            arrayList.add("TZE");
            arrayList.add("Wivo");
            arrayList.add("Notorola");
        }

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recview.setLayoutManager(layoutManager);
        ManufacturersAdapter adapter = new ManufacturersAdapter(arrayList);
        binding.recview.setAdapter(adapter);

        binding.setUserNameBtn.setOnClickListener(view -> ChangeUsernameDialog.showDialog(getActivity()));
        binding.googleFormBtn.setOnClickListener(view -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.google_form), R.color.md_theme_light_onSecondary));
        binding.buySubscriptionBtn.setOnClickListener(view -> {billingConnector.subscribe(requireActivity(), "month_plus");});

        initBillingClient();

        return binding.getRoot();
    }

    private void initBillingClient() {
        List<String> subscriptionIds = new ArrayList<>();
        subscriptionIds.add("month_plus");

        billingConnector = new BillingConnector(requireActivity(), LICENSE_KEY) //"license_key" - public developer key from Play Console
                //.setConsumableIds(consumableIds) //to set consumable ids - call only for consumable products
                //.setNonConsumableIds(nonConsumableIds) //to set non-consumable ids - call only for non-consumable products
                .setSubscriptionIds(subscriptionIds) //to set subscription ids - call only for subscription products
                .autoAcknowledge() //legacy option - better call this. Alternatively purchases can be acknowledge via public method "acknowledgePurchase(PurchaseInfo purchaseInfo)"
                .autoConsume() //legacy option - better call this. Alternatively purchases can be consumed via public method consumePurchase(PurchaseInfo purchaseInfo)"
                .enableLogging() //to enable logging for debugging throughout the library - this can be skipped
                .connect(); //to connect billing client with Play Console

        billingConnector.setBillingEventListener(new BillingEventListener() {
            @Override
            public void onProductsFetched(@NonNull List<ProductInfo> productDetails) {
                String product;
                String price;
                if (!SharedPreferencesUtils.getBoolean(requireActivity(), "isAdFree"))
                binding.buySubscriptionBtn.setVisibility(View.VISIBLE);

                for (ProductInfo productInfo : productDetails) {
                    product = productInfo.getName();
                    price = productInfo.getSubscriptionOfferPrice(0, 0);

                    if (product.equalsIgnoreCase("month_plus")) {
                        Log.d("BillingConnector", "Product fetched: " + product);
                        Log.d("BillingConnector", "Product price: " + price);
                        binding.removeAdsPrice.setText(product + "," + "\n" + price);
                    }
                    fetchedProductInfoList.add(productInfo); //check "usefulPublicMethods" to see how to synchronously check a purchase state
                }
            }

            @Override
            public void onPurchasedProductsFetched(@NonNull ProductType productType, @NonNull List<PurchaseInfo> purchases) {
                String product;
                for (PurchaseInfo purchaseInfo : purchases) {
                    product = purchaseInfo.getProduct();

                    if (product.equalsIgnoreCase("month_plus")) {
                        Log.d("BillingConnector", "Purchased product fetched: " + product);
                    }
                }
            }

            @Override
            public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {
                String product;
                String purchaseToken;

                for (PurchaseInfo purchaseInfo : purchases) {
                    product = purchaseInfo.getProduct();
                    purchaseToken = purchaseInfo.getPurchaseToken();

                    if (product.equalsIgnoreCase("month_plus")) {
                        Log.d("BillingConnector", "Product purchased: " + product);
                        Log.d("BillingConnector", "Purchase token: " + purchaseToken);
                        SharedPreferencesUtils.saveBoolean(getActivity(), "isAdFree", true);
                    }
                    purchasedInfoList.add(purchaseInfo); //check "usefulPublicMethods" to see how to acknowledge or consume a purchase manually
                }
            }

            @Override
            public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {
                //TODO - acknowledge purchase
            }

            @Override
            public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
                String consumedProduct = purchase.getProduct();

                if (consumedProduct.equalsIgnoreCase("month_plus")) {
                    Log.d("BillingConnector", "Consumed: " + consumedProduct);
                    Toast.makeText(requireActivity(), "Consumed: " + consumedProduct, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingError(@NonNull BillingConnector billingConnector, @NonNull BillingResponse response) {
                switch (response.getErrorType()) {
                    case ACKNOWLEDGE_WARNING:
                        //this response will be triggered when the purchase is still PENDING
                        Toast.makeText(requireActivity(), "The transaction is still pending. Please come back later to receive the purchase!", Toast.LENGTH_SHORT).show();
                        break;
                    case BILLING_UNAVAILABLE:
                    case SERVICE_UNAVAILABLE:
                        Toast.makeText(requireActivity(), "Billing is unavailable at the moment. Check your internet connection!", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(requireActivity(), "Something happened, the transaction was canceled!", Toast.LENGTH_SHORT).show();
                        break;
                    case USER_CANCELED:
                        Toast.makeText(requireActivity(), "The transaction was canceled!", Toast.LENGTH_SHORT).show();
                        break;
                }

                Log.d("BillingConnector", "Error type: " + response.getErrorType() +
                        " Response code: " + response.getResponseCode() + " Message: " + response.getDebugMessage());
            }
        });
    }

    /*
     * Check this method to learn how to implement useful public methods
     * provided by 'google-inapp-billing' library
     * */
    @SuppressWarnings("unused")
    private void usefulPublicMethods() {
        /*
         * public final boolean isReady()
         *
         * Returns the state of the billing client
         * */
        if (billingConnector.isReady()) {
            //TODO - do something
            Log.d("BillingConnector", "Billing client is ready");
        }

        /*
         * public SupportState isSubscriptionSupported()
         *
         * To check device-support for subscriptions (not all devices support subscriptions)
         * */
        if (billingConnector.isSubscriptionSupported() == SupportState.SUPPORTED) {
            //TODO - do something
            Log.d("BillingConnector", "Device subscription support: SUPPORTED");
        } else if (billingConnector.isSubscriptionSupported() == SupportState.NOT_SUPPORTED) {
            //TODO - do something
            Log.d("BillingConnector", "Device subscription support: NOT_SUPPORTED");
        } else if (billingConnector.isSubscriptionSupported() == SupportState.DISCONNECTED) {
            //TODO - do something
            Log.d("BillingConnector", "Device subscription support: client DISCONNECTED");
        }

        /*
         * public final PurchasedResult isPurchased(ProductInfo productInfo)
         *
         * To synchronously check a purchase state
         * */
        for (ProductInfo productInfo : fetchedProductInfoList) {
            if (billingConnector.isPurchased(productInfo) == PurchasedResult.YES) {
                //TODO - do something
                Log.d("BillingConnector", "The product: " + productInfo.getProduct() + " is purchased");
            } else if (billingConnector.isPurchased(productInfo) == PurchasedResult.NO) {
                //TODO - do something
                Log.d("BillingConnector", "The product: " + productInfo.getProduct() + " is not purchased");
            } else if (billingConnector.isPurchased(productInfo) == PurchasedResult.CLIENT_NOT_READY) {
                //TODO - do something
                Log.d("BillingConnector", "Cannot check: " + productInfo.getProduct() + " because client is not ready");
            } else if (billingConnector.isPurchased(productInfo) == PurchasedResult.PURCHASED_PRODUCTS_NOT_FETCHED_YET) {
                //TODO - do something
                Log.d("BillingConnector", "Cannot check: " + productInfo.getProduct() + " because purchased products are not fetched yet");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (billingConnector != null) {
            billingConnector.release();
        }
    }
}