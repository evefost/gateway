package feign;

import java.io.IOException;

/**
 * proxy client
 * <p>
 *
 * @author xieyang
 * @version 1.0.0
 * @date 2019/11/5
 */
public class DelegateClient implements Client {

    private Client balanceClient;

    private Client defaultClient;

    private boolean balance;

    private String balanceUrl;

    private String directUrl;


    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        if (balance) {
            return balanceClient.execute(request, options);
        }
        Request newRequest = directRequest(request);
        return defaultClient.execute(newRequest, options);
    }

    private Request directRequest(Request srcRequest) {
        String url = srcRequest.url().replaceAll(balanceUrl, directUrl);
        Request newRequest = new Request(srcRequest.method(), url, srcRequest.headers(), srcRequest.body(), srcRequest.charset());
        return newRequest;
    }



    public Client getBalanceClient() {
        return balanceClient;
    }

    public void setBalanceClient(Client balanceClient) {
        this.balanceClient = balanceClient;
    }

    public Client getDefaultClient() {
        return defaultClient;
    }

    public void setDefaultClient(Client defaultClient) {
        this.defaultClient = defaultClient;
    }

    public boolean isBalance() {
        return balance;
    }

    public void setBalance(boolean balance) {
        this.balance = balance;
    }

    public String getBalanceUrl() {
        return balanceUrl;
    }

    public void setBalanceUrl(String balanceUrl) {
        this.balanceUrl = balanceUrl;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }


}
