package com.lucho.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * TilesView extension that handles content type.
 *
 * @author Luciano.Leggieri
 */
public final class TilesView extends
        org.springframework.web.servlet.view.tiles2.TilesView {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void renderMergedOutputModel(
            final Map<String, Object> model,
            final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        response.setContentType(this.getContentType());
        super.renderMergedOutputModel(model, request, response);
    }

}
