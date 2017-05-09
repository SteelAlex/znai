package com.twosigma.documentation.extensions;

import com.twosigma.utils.JsonUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author mykola
 */
public class PluginParams {
    private String pluginId;
    private String freeParam;
    private PluginParamsOpts opts;

    public PluginParams(String pluginId, String value) {
        this.pluginId = pluginId;
        this.freeParam = extractFreeParam(value);
        this.opts = new PluginParamsOpts(extractMap(value));
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getFreeParam() {
        return freeParam;
    }

    public PluginParamsOpts getOpts() {
        return opts;
    }

    private String extractFreeParam(String value) {
        int optsStartIdx = value.indexOf('{');
        return (optsStartIdx != -1 ?
                value.substring(0, optsStartIdx):
                value).trim();
    }

    private Map<String, ?> extractMap(String value) {
        int optsStartIdx = value.indexOf('{');
        if (optsStartIdx == -1) {
            return Collections.emptyMap();
        }

        String json = value.substring(optsStartIdx);
        return JsonUtils.deserializeAsMap(json);
    }
}