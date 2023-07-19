package com.qzbaozi.api.swagger.plugin;

import springfox.documentation.service.ResolvedMethodParameter;

import java.util.List;

/**
 * @author sfh
 * @date 2023/7/14 10:41
 */
public class PluginUtil {

    public static String getClassName(String name, List<ResolvedMethodParameter> parameters) {
        //gen
        String supperName = "";
        if (name != null && !"".equals(name)) {
            if (name.length() == 1) {
                supperName = name.toUpperCase();
            } else {
                supperName = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        StringBuilder builder = new StringBuilder(128);
        builder.append(supperName);
        for (ResolvedMethodParameter parameter : parameters) {
            builder.append(parameter.getParameterType().getErasedType().getSimpleName());
        }

        return builder.toString();
    }

    /**左模糊
     * @param url
     * @param uriPrefix
     * @return
     */
    public static boolean startsWith(String url, String uriPrefix) {
        return url.startsWith("/" + uriPrefix);
    }
}
