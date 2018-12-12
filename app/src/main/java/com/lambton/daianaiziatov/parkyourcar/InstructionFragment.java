package com.lambton.daianaiziatov.parkyourcar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class InstructionFragment extends Fragment {

    private WebView webView;
    private String js = "javascript:function listenForLoad(){var url = window.location.pathname;var isLoaded = false;var loadReq;if (url === '/' && !isLoaded) {loadReq = window.requestAnimationFrame(listenForLoad);} else {isLoaded = true;var content = document.getElementsByClassName('container info-page');for(var i = 0; i < content.length; i++){content[i].style.top = '0px';}var header = document.getElementsByClassName('header');while(header.length > 0){header[0].parentNode.removeChild(header[0]);}var footer = document.getElementById('footer'); footer.parentNode.removeChild(footer);while(footer.length > 0){footer[0].parentNode.removeChild(footer[0]);}return;}}listenForLoad();";


    public InstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View instructionView = inflater.inflate(R.layout.fragment_instruction, container, false);

        webView = instructionView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl(js);
            }
        });
        webView.loadUrl("https://www.planyo.com/tutorial-prices.php");

        return instructionView;
    }

}
