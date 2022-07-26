package com.cell.component.bot.twitter.config;

import com.cell.base.common.utils.StringUtils;
import com.cell.sdk.configuration.Configuration;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TwitterBotConfig
{
    public static final String twitterBotModule = "twitterBot";
    private boolean post = false;
    private List<TwitterBotNode> nodes;
    private int postPerLimits = 30;
    private int incremental;
    private boolean ordered;
    private int initDelay = 30;

    // 抓取的关键字
    private String profileKey;

    @Data
    public static class TwitterBotNode
    {
        private String accessToken;
        private String accessTokenSecret;
        private String bearerToken;
        private String apiKey;
        private String apiSecret;
        private String callBackUrl;
        private boolean active = true;

        private TwitterClient twitterClient;
    }

    public static TwitterBotConfig getInstance()
    {
        if (TwitterBotConfigFactory.prev != null)
        {
            synchronized (TwitterBotConfig.class)
            {
                if (TwitterBotConfigFactory.prev != null)
                {
                    TwitterBotConfigFactory.instance = TwitterBotConfigFactory.prev;
                    TwitterBotConfigFactory.prev = null;
                }
            }
        }
        return TwitterBotConfigFactory.instance;
    }

    public static class TwitterBotConfigFactory
    {
        private static TwitterBotConfig prev = null;
        private static TwitterBotConfig instance = new TwitterBotConfig();
    }

    public static void seal()
    {
        TwitterBotConfig cfg = Configuration.getDefault().getAndMonitorConfig(twitterBotModule, TwitterBotConfig.class, (insta) ->
        {
            try
            {
                insta.onSeal();
                synchronized (TwitterBotConfig.class)
                {
                    TwitterBotConfigFactory.prev = insta;
                }
            } catch (Exception e)
            {

            }
        });
        TwitterBotConfigFactory.instance = cfg;
        TwitterBotConfigFactory.instance.onSeal();
    }

    private void onSeal()
    {
        if (StringUtils.isEmpty(this.profileKey)){
            throw new RuntimeException();
        }
        for (TwitterBotNode node : nodes)
        {
            TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                    .bearerToken(node.getBearerToken())
                    .accessToken(node.getAccessToken())
                    .accessTokenSecret(node.getAccessTokenSecret())
                    .apiKey(node.getApiKey())
                    .apiSecretKey(node.getApiSecret())
                    .build());
            node.setTwitterClient(twitterClient);
        }
        if (this.postPerLimits <= 0)
        {
            this.postPerLimits = 30;
        }

    }

    private List<TwitterBotNode> getCopyShuffleNodes()
    {
        List<TwitterBotNode> ret = new ArrayList<>(this.nodes);
        Collections.shuffle(ret);
        return ret;
    }

    public TwitterBotNode getActiveShuffleNode()
    {
        List<TwitterBotNode> copyShuffleNodes = this.getCopyShuffleNodes();
        for (TwitterBotNode copyShuffleNode : copyShuffleNodes)
        {
            if (copyShuffleNode.active)
            {
                return copyShuffleNode;
            }
        }
        return null;
    }
}

