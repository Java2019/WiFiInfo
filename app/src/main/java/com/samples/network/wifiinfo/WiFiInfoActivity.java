package com.samples.network.wifiinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class WiFiInfoActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener{

    private TextView text;
    private CheckBox checkBox;
    private WifiManager manager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            // Изменение статуса сети Wi-Fi
            switch(wifiState){
                case WifiManager.WIFI_STATE_ENABLING:
                    text.setText("Wi-Fi state enabling");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    text.setText("Wi-Fi state enabled");
                    text.append(printWifiInfo());
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    text.setText("Wi-Fi state disabling");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    text.setText("Wi-Fi state disabled");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    text.setText("Wi-Fi state unknown");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_info);

        text = (TextView)findViewById(R.id.text);
        checkBox = (CheckBox)findViewById(R.id.cbEnable);
        manager = (WifiManager)getSystemService(WIFI_SERVICE);

        this.registerReceiver(this.receiver,
                new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        checkBox.setChecked(manager.isWifiEnabled());
        checkBox.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        manager.setWifiEnabled(isChecked);
    }

    // Вывод информации на экран
    private String printWifiInfo() {
        StringBuilder sb = new StringBuilder();
        WifiInfo wifiInfo = manager.getConnectionInfo();
        DhcpInfo dhcpInfo = manager.getDhcpInfo();
        sb.append("\nWi-Fi Information:");
        sb.append("\n\tMAC Address:\t" + wifiInfo.getMacAddress());
        sb.append("\n\tSS ID:\t" + wifiInfo.getSSID());
        sb.append("\n\tBSS ID:\t" + wifiInfo.getBSSID());
        sb.append("\n\tLink speed:\t" + wifiInfo.getLinkSpeed());
        sb.append("\n\tLink speed units:\t" + WifiInfo.LINK_SPEED_UNITS);
        sb.append("\n\tRSSI:\t" + wifiInfo.getRssi());
        sb.append("\n\tHidden SSID:\t" + wifiInfo.getHiddenSSID());
        sb.append("\n\tNetwork ID:\t" + wifiInfo.getNetworkId());
        sb.append("\n\nDHCP Information");
        sb.append("\n\tIP address:\t" + convertIpAddress(dhcpInfo.ipAddress));
        sb.append("\n\tDNS 1:\t" + convertIpAddress(dhcpInfo.dns1));
        sb.append("\n\tDNS 2:\t" + convertIpAddress(dhcpInfo.dns2));
        sb.append("\n\tGateway:\t" + convertIpAddress(dhcpInfo.gateway));
        sb.append("\n\tLease duration:\t" + dhcpInfo.leaseDuration);
        sb.append("\n\tDescribe contents:\t" + dhcpInfo.describeContents());
        return sb.toString();
    }
    // Метод для конвертирования IP-адреса в октетную форму
    private String convertIpAddress(int ipAddress) {
        int ip0, ip1, ip2, ip3, tmp;

        ip3 = ipAddress / 0x1000000;;
        tmp = ipAddress % 0x1000000;
        ip2 = tmp / 0x10000;;
        tmp %= 0x10000;
        ip1 = tmp / 0x100;;
        ip0 = tmp % 0x100;

        return String.format("%d.%d.%d.%d", ip0, ip1, ip2, ip3);
    }
}
