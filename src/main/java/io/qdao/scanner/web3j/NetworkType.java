package io.qdao.scanner.web3j;

public enum NetworkType {

    MAINNET(NetworkType.MAINNET_URL_PREFIX),
    ROPSTEN(NetworkType.ROPSTEN_URL_PREFIX),
    RINKEBY(NetworkType.RINKEBY_URL_PREFIX),
    KOVAN(NetworkType.KOVAN_URL_PREFIX),
    GOERLI(NetworkType.GOERLI_URL_PREFIX);


    private static final String MAINNET_URL_PREFIX = "mainnet";
    private static final String ROPSTEN_URL_PREFIX = "ropsten";
    private static final String RINKEBY_URL_PREFIX = "rinkeby";
    private static final String KOVAN_URL_PREFIX = "kovan";
    private static final String GOERLI_URL_PREFIX = "goerli";

    private static final String IPFS_GATEWAY_URL = "https://ipfs.infura.io:5001/api/";
    private static final String IPFS_API_URL = "https://ipfs.infura.io/ipfs/";

    private final String httpsUrl;
    private final String wssUrl;

    private NetworkType(String networkName) {
        httpsUrl = makeHttpsUrlByNetwork(networkName);
        wssUrl = makeWssUrlByNetwork(networkName);
    }

    public String getHttpsUrl(String projectId) {
        return String.format("%s/%s", httpsUrl, projectId);
    }

    public String getWssUrl(String projectId) {
        return String.format("%s/%s", wssUrl, projectId);
    }

    private static String makeWssUrlByNetwork(String networkName) {
        return String.format("wss://%s.infura.io/ws/v3", networkName);
    }

    private static String makeHttpsUrlByNetwork(String networkName) {
        return String.format("https://%s.infura.io/v3", networkName);
    }

    public static String getIPFSGatewayUrl() {
        return IPFS_GATEWAY_URL;
    }

    public static String getIPFSApiUrl() {
        return IPFS_API_URL;
    }


}