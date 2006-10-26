<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %><%@ page import="java.io.StringWriter"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.util.List"%><%@ page import="java.util.Iterator"%><%@ page import="com.opensymphony.module.sitemesh.Page"%><%@ page import="com.opensymphony.module.sitemesh.RequestConstants"%><%@ page import="com.opensymphony.module.sitemesh.util.OutputConverter"%><%@ page import="no.schibstedsok.searchportal.view.i18n.TextMessages"%><%@ page import="no.schibstedsok.searchportal.view.output.VelocityResultHandler"%><%@ page import="no.schibstedsok.searchportal.run.RunningQuery" %><%@ page import="no.schibstedsok.searchportal.result.Enrichment"%><%@ page import="no.schibstedsok.searchportal.result.Modifier"%><%@ page import="no.schibstedsok.searchportal.site.Site"%><%@ page import="org.apache.commons.lang.StringEscapeUtils" %><%@ page import="org.apache.velocity.Template"%><%@ page import="org.apache.velocity.VelocityContext"%><%@ page import="org.apache.velocity.app.VelocityEngine"%><%@ page import="no.schibstedsok.searchportal.view.velocity.VelocityEngineFactory" %><%
    // Do not format this file. Cannot have whitespace being outputted in the rss.
    final Site site = (Site) request.getAttribute(Site.NAME_KEY);
    final Page siteMeshPage = (Page) request.getAttribute(RequestConstants.PAGE);
    final TextMessages text = (TextMessages) request.getAttribute("text");
    final VelocityEngine engine = VelocityEngineFactory.valueOf(site).getEngine();
    final Template template = VelocityResultHandler.getTemplate(engine, site, "/pages/main");
    final RunningQuery query = (RunningQuery) request.getAttribute("query");
    final List sources = query.getSources();

    if (template != null) {
        final VelocityContext context = VelocityResultHandler.newContextInstance(engine);

        for (Iterator iter = sources.iterator(); iter.hasNext();) {
            Modifier mod = (Modifier) iter.next();
            if (mod.getName().equals("sesam_hits")) {
                context.put("sesam_hits", text.getMessage("numberFormat", new Integer(mod.getCount())));
            }
        }
        context.put("request", request);
        context.put("response", response);
        context.put("page", siteMeshPage);
        context.put("base", request.getContextPath());
        context.put("title", OutputConverter.convert(siteMeshPage.getTitle()));
        context.put("text", text);
        context.put("currentTab", query.getSearchTab());
        context.put("runningQuery", query);
        context.put("query", query.getQueryString());
        context.put("enrichments", query.getEnrichments());

        {
            final StringWriter buffer = new StringWriter();
            siteMeshPage.writeBody(OutputConverter.getWriter(buffer));
            context.put("body", buffer.toString());
        }
        template.merge(context, out);
    }%>