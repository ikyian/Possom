// Copyright (2006) Schibsted Søk AS
package no.schibstedsok.searchportal.view.velocity;

import org.apache.log4j.Logger;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.MethodInvocationException;

import java.io.Writer;
import java.io.IOException;

import no.schibstedsok.searchportal.result.Linkpulse;

/**
 * Created by IntelliJ IDEA.
 * User: SSTHKJER
 * Date: 03.apr.2006
 * Time: 09:21:04
 * To change this template use File | Settings | File Templates.
 */
public final class AccountingDirective extends Directive {

    private static final Logger LOG = Logger.getLogger(AccountingDirective.class);


    private static final String NAME = "accounting";

    /**
     * returns the name of the directive.
     *
     * @return the name of the directive.
     */
    public String getName() {
        return NAME;
    }

    /**
     * returns the type of the directive. The type is LINE.
     * @return The type == LINE
     */
    public int getType() {
        return LINE;
    }

    /**
     * Renders the html for the accountings.
     * This isn't an optimal solution, but the way data is stored
     * it was the fastest way to do it. For me.
     *
     * @param context
     * @param writer
     * @param node
     *
     * @throws java.io.IOException
     * @throws org.apache.velocity.exception.ResourceNotFoundException
     * @throws org.apache.velocity.exception.ParseErrorException
     * @throws org.apache.velocity.exception.MethodInvocationException
     * @return the encoded string.
     */
    public boolean render(final InternalContextAdapter context, final Writer writer, final Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        if (node.jjtGetNumChildren() != 1) {
            rsvc.error("#" + getName() + " - wrong number of arguments");
            return false;
        }

        // The text string from datafield which all the accountingsnumber is stored
        final String s = node.jjtGetChild(0).value(context).toString();

        String html = "";
        html = "<table bgcolor=\"#CCCCCC\" cellspacing=\"1\">";

        // New line seperator
        final String[] row = s.split("#sepnl#");
        String[] col;
        boolean bgcolor = false;
        String text = "";
        boolean quitLoop = false;

        // use linkpulse to LOG lindorff logo click
        final Linkpulse linkpulse = (Linkpulse) context.get("linkpulse");
        final String lpUrl = linkpulse.getUrl("http://www.lindorff.no/", "category:static;subcategory:provider", "sgo", "ext");

        //print rows
        for (int i = 0; i < row.length; i++) {

            // column seperator
            col = row[i].split("#sep#");

            //print columns
            for (int k = 0; k < col.length; k++) {

                // do some tests before each new row
                if (k==0) {
                    // #balance0# means print a new table
                    if (col[0].indexOf("#balanse0#") > -1) {
                        html += "</table> <table id=\"balance\" bgcolor=\"#CCCCCC\" cellspacing=\"1\">";
                        bgcolor = false;
                    // #balance1# means don't show rest of the numbers
                    } else if (col[0].indexOf("#balanse1#") > -1) {
                        quitLoop = true;
                        break;
                    }
                    // checks if the row should be bold
                    if (col[0].indexOf("#bold#") == -1) {
                        text = col[0];
                        html += "<tr>";
                    } else {
                        html += "<tr class=\"bold_line\">";
                        text = col[0].substring(col[0].indexOf("#bold#")+6);
                    }
                } else
                    text = col[k];

                if (!bgcolor) {
                    html += "<td class=\"col"+ (k+1) + "\" bgcolor=\"#FFFFFF\">" + text.trim() + "</td>";
                } else {
                    html += "<td class=\"col"+ (k+1) + "\" style=\"background-color: #EBEBEB;\">" + text.trim() + "</td>";
                }
            }
            if (!quitLoop) {
                bgcolor = !bgcolor;
                html += "</tr>";
            } else
                break;
        }
        html += "</table>";
        html += "<div id=\"lindorff\">* Alle tall i hele 1000 kroner.<br />* Regnskapet viser kun hovedtall, og er levert av Lindorff Decision.</div>";
        html += "<div style=\"clear:both; padding-top:4px;\"><a href=\"" + lpUrl + "\" target=\"_blank\"><img src=\"../images/lindorff_logo.gif\" alt=\"Linforff logo\" /></a></div>";

        writer.write(html);
        final Token lastToken = node.getLastToken();

        if (lastToken.image.endsWith("\n")) {
            writer.write("\n");
        }

        return true;
    }
}