<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:error="http://www.dke.jku.at">
	
	<xsl:output indent="no" method="html" omit-xml-declaration="yes"/>

	<!-- most general template -->
	<xsl:template match="node()|@*">
		<xsl:apply-templates/>
	</xsl:template>
	
	<!-- template for root element -->
	<xsl:template match="/">
		<xsl:call-template name="print-html"/>	
	</xsl:template>
	
	<!-- template for generating an html document -->
	<xsl:template name="print-html">
		<xsl:param name="hasSyntaxErrors" select="./xquery-result/result/@syntax-error = 1 or ./xquery-result/result/@syntax-error = 'true'"/>
		<xsl:param name="showDetails" select="./xquery-result/@diagnose-level = 3"/>
		<xsl:param name="hideResult" select="./xquery-result/@mode = 'submit'"/>
		<html>
			<head>
				<title>XQuery Result</title>
				<link rel='stylesheet' type='text/css' href='/etutor/css/etutor.css'/>
				<style>
					<xsl:text>&lt;!--</xsl:text>
						*.missing {background:#F6D8AC}
						*.displaced {background:#FFFFDD}
						*.redundant {background:#ECF8CC}
						*.incorrect-value {background:#D5F08A}
						*.indent  {margin-left:1em; text-indent:-1em; margin-right:1em}
						*.indent-element {margin-left:1em;text-indent:-2em}
						*.element  {color:#990000}
						*.attribute  {color:black}
						*.xsl-attribute {color:#990099}
						*.mark  {color:blue}
						*.text {font-weight:bold}
						*.p-instruction {color:blue}
						*.comment {font:small Courier; color:#888888; margin:0px; display:inline}
						*.result-box {
							border-width: 4px;
							border-style: double;
							border-color: #d3d3d3;
							padding: 4px; margin: 0em }						
						<!--					
						*.predicate-error {background:#ECF8CC}
						-->
					<xsl:text>--&gt;</xsl:text>
				</style>
			</head>
			<body>
				<table cellpadding="3" cellspacing="1" border="0">
					<xsl:if test="string-length(normalize-space(./xquery-result/analysis/summary/detail)) > 0">
						<tr>
							<td>
								<table>
									<b>RESULT</b>
									<xsl:if test="string-length(normalize-space(./xquery-result/analysis/summary/header)) > 0">
										<tr>
											<td width="30"/>
											<td>
												<xsl:value-of select="./xquery-result/analysis/summary/header"/>
											</td>
										</tr>																		
									</xsl:if>
									<tr>
										<td width="30"/>
										<td>
											<xsl:value-of select="./xquery-result/analysis/summary/detail"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="string-length(normalize-space(./xquery-result/analysis/grading/detail)) > 0">
						<tr>
							<td>
								<table>
									<b>GRADING</b>
									<xsl:if test="string-length(normalize-space(./xquery-result/analysis/grading/header)) > 0">
										<tr>
											<td width="30"/>
											<td>
												<xsl:value-of select="./xquery-result/analysis/grading/header"/>
											</td>
										</tr>																		
									</xsl:if>
									<tr>
										<td width="30"/>
										<td>
											<xsl:value-of select="./xquery-result/analysis/grading/detail"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="string-length(normalize-space(./xquery-result/analysis/syntax/detail)) > 0">
						<tr>
							<td>
								<table>
									<b>SYNTAX ERRORS</b>
									<tr>
										<td width="30">
										</td>
										<td>
											<table>
												<xsl:if test="string-length(normalize-space(./xquery-result/analysis/syntax/header)) > 0">
													<tr>
														<td style="vertical-align:top">
															<i>Error:</i>
														</td>
														<td>
															<xsl:value-of select="./xquery-result/analysis/syntax/header"/>
														</td>
													</tr>																		
												</xsl:if>
												<tr>
													<td style="vertical-align:top">
														<i>Description:</i>
													</td>
													<td>
														<pre><xsl:value-of select="./xquery-result/analysis/syntax/detail"/></pre>
													</td>
												</tr>									
											</table>
										</td>									
									</tr>
								</table>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="not($hideResult)">
						<xsl:if test="./xquery-result/analysis/error">
							<tr>
								<td>								
									<table>
										<b>SEMANTIC ERRORS</b>
										<xsl:for-each select="./xquery-result/analysis/error[string-length(normalize-space(detail)) > 0]">
											<tr>
												<td width="30">
												</td>
												<td>
													<table>
														<xsl:if test="string-length(normalize-space(header)) > 0">				
															<tr>
																<td style="vertical-align:top">
																	<i>Error:</i>
																</td>
																<td>
																	<xsl:choose>
																		<xsl:when test="$showDetails and string-length(normalize-space(@code)) > 0">
																			<xsl:element name="span">
																				<xsl:attribute name="class"><xsl:value-of select="@code"/></xsl:attribute>
																				<xsl:value-of select="header"/><br/>
																			</xsl:element>
																		</xsl:when>
																		<xsl:otherwise>
																			<xsl:value-of select="header"/><br/>
																		</xsl:otherwise>
																	</xsl:choose>
																</td>
															</tr>
														</xsl:if>
														<tr>
															<td style="vertical-align:top">
																<i>Description:</i>
															</td>
															<td>
																<pre><xsl:value-of select="detail"/></pre>
															</td>
														</tr>									
													</table>
												</td>								
											</tr>
										</xsl:for-each>
									</table>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="not($hasSyntaxErrors)">
							<tr>
								<td>
									<table>
										<b>
											<xsl:choose>
												<xsl:when test="$showDetails">ANALYZED QUERY RESULT</xsl:when>
												<xsl:otherwise>QUERY RESULT</xsl:otherwise>
											</xsl:choose>
										</b>
										<tr>
											<td width="30"/>
											<td class="result-box">
												<xsl:choose>
													<!-- result is just a text, maybe inside a CDATA section -->
													<xsl:when test="./xquery-result/result/text()">
														<pre><xsl:apply-templates select="./xquery-result/result/node()|@*"/></pre>
													</xsl:when>
													<!-- no result -->
													<xsl:when test="not(./xquery-result/result/*)">
														 &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
													</xsl:when>
													<!-- transforming the xquery result -->
													<xsl:otherwise>
														<xsl:apply-templates select="./xquery-result/result/node()|@*"/>
													</xsl:otherwise>											
												</xsl:choose>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</xsl:if>
					</xsl:if>						
				</table>
			</body>
		</html>
	</xsl:template>
	
	
	<!-- swallow attributes which define the 'error' namespace within some element -->
	<xsl:template match="@xmlns:error" priority="25"/>
	
	<!-- swallow attributes in the 'error:*' namespace, only used for highlighting attributes with errors -->
	<xsl:template match="@error:*" priority="25"/>
	
	<!-- swallow 'error:node' nodes, which are used for marking an error of the XQuery result -->
	<xsl:template match="error:node[@error]" priority="25">
		<!-- indicates if the node which matches this template, has to be output in the same line as the parent
		(for example the text value of a text node) or in a new line with indent (like a child element of an element) -->
		<xsl:param name="indent"/>
		<xsl:choose>
			<xsl:when test="$indent = 'no'">
				<span class="{@error}">
					<xsl:apply-templates select="node()">
						<xsl:with-param name="indent" select="$indent"/>
					</xsl:apply-templates>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<div class="{@error}">
					<xsl:apply-templates select="node()">
						<xsl:with-param name="indent" select="$indent"/>
					</xsl:apply-templates>
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for processing-instructions -->
	<xsl:template match="processing-instruction()">
		<div class="indent">
			<span class="mark">
				<xsl:text>&lt;?</xsl:text>
			</span>
			<span class="p-instruction">
				<xsl:value-of select="name(.)"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="."/>
			</span>
			<span class="mark">
				<xsl:text>?&gt;</xsl:text>
			</span>
		</div>
	</xsl:template>

	<!-- template for xml processing instruction -->
	<xsl:template match="processing-instruction('xml')">
		<div class="indent">
			<span class="mark">
				<xsl:text>&lt;?</xsl:text>
			</span>
			<span class="p-instruction">
				<xsl:text>xml </xsl:text>
				<xsl:for-each select="@*">
					<xsl:value-of select="name(.)"/>
					<xsl:text>="</xsl:text>
					<xsl:value-of select="."/>
					<xsl:text>" </xsl:text>
				</xsl:for-each>
			</span>
			<span class="mark">
				<xsl:text>?gt;</xsl:text>
			</span>
		</div>
	</xsl:template>

	<!-- template for attributes not handled elsewhere -->
	<xsl:template name="attributes-template">
		<!-- used for indicating that the value of the transformed attribute is somehow incorrect in the XQuery result -->
		<xsl:param name="error"/>
		<xsl:element name="span">
			<xsl:attribute name="class"><xsl:if test="xsl:*/@*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>attribute</xsl:text></xsl:attribute>
			<xsl:value-of select="name(.)"/>
		</xsl:element>
		<span class="mark">="</span>
		<B>
			<xsl:choose>
				<xsl:when test="$error = 'incorrect-value'">
					<xsl:element name="span">
						<xsl:attribute name="class"><xsl:value-of select="$error"/></xsl:attribute>
						<xsl:value-of select="."/>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</B>
		<span class="mark">"</span>
	</xsl:template>

	<!-- general template for attributes which possibly might have to be highlighted as incorrect attribute; 
	This is true if the parent of this attribute contains another attribute with the same name, but prefixed 
	with the 'error' prefix. The value of this 'error'-attribute indicates the type of error. -->
	<xsl:template match="@*">
		<xsl:param name="name" select="name()"/>
		<xsl:param name="error" select="../@error:*[local-name()=$name]"/>
		<xsl:choose>
			<xsl:when test="$error = 'missing' or $error = 'redundant'">
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:value-of select="$error"/></xsl:attribute>
					<xsl:call-template name="attributes-template">
						<xsl:with-param name="error" select="$error"/>
					</xsl:call-template>
				</xsl:element>
			</xsl:when>
			<!-- the error type is expected to be 'incorrect-value', if not 'missing' or 'redundant' -->
			<xsl:otherwise>
				<xsl:call-template name="attributes-template">
					<xsl:with-param name="error" select="$error"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for text nodes -->
	<xsl:template match="text()">
		<xsl:param name="indent"/>
		<xsl:if test="$indent = 'no'">
			<span class="text">
				<xsl:value-of select="."/>
			</span>
		</xsl:if>
		<xsl:if test="$indent = '' or indent = 'yes'">
			<div class="indent">
				<!--	<span class="b"> </span> -->
				<span class="text">
					<xsl:value-of select="."/>
				</span>
			</div>
		</xsl:if>
		<!-- xsl:otherwise not working ? -->
	</xsl:template>

	<!-- template for comment nodes -->
	<xsl:template match="comment()">
		<div class="indent">
			<span>
				<span class="mark">
					<xsl:text>&lt;!--</xsl:text>
				</span>
			</span>
			<span class="comment" id="clean">
				<pre>
					<xsl:value-of select="."/>
				</pre>
			</span>
			<span class="mark">
				<xsl:text>--&gt;</xsl:text>
			</span>
		</div>
	</xsl:template>
	
	<!-- template for elements not handled elsewhere (leaf nodes) -->
	<xsl:template match="*">
		<div class="indent">
			<div class="indent-element">
				<span class="mark">&lt;</span>
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
					<xsl:value-of select="name(.)"/>
					<xsl:if test="@*">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:element>
				<xsl:for-each select="@*">
					<xsl:apply-templates select="."/>
					<xsl:if test="position() != last()">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<span class="mark">
					<xsl:text>/&gt;</xsl:text>
				</span>
			</div>
		</div>
	</xsl:template>

	<!-- template for elements with comment, pi and/or cdata children -->
	<xsl:template match="*[node()]">
		<div class="indent">
			<span class="mark">&lt;</span>
			<xsl:element name="span">
				<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
				<xsl:value-of select="name(.)"/>
				<xsl:if test="@*">
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:element>
			<xsl:for-each select="@*">
				<xsl:apply-templates select="."/>
				<xsl:if test="position() != last()">
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:for-each>
			<span class="mark">
				<xsl:text>&gt;</xsl:text>
			</span>
		</div>
		<div>
			<xsl:apply-templates/>
			<div>
				<span class="mark">
					<xsl:text>&lt;/</xsl:text>
				</span>
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
					<xsl:value-of select="name(.)"/>
				</xsl:element>
				<span class="mark">
					<xsl:text>&gt;</xsl:text>
				</span>
			</div>
		</div>
	</xsl:template>

	<!-- template which is needed to guarantee that the transformed XML tree is output prettily. This is required for the special case, when
	some node only contains an 'error'-prefixed element which in turn contains a text node; This guarantees that the text node will be printed in the same line
	as the parent element; -->
	<xsl:template match="*[count(*) = 1 and *[namespace-uri() = 'http://www.dke.jku.at' and text() and not(comment() or processing-instruction())]]" priority="25">
		<xsl:call-template name="only-text-template"/>
	</xsl:template>
	
	<!-- template for elements with only text children -->
	<xsl:template match="*[text() and not (comment() or processing-instruction())]" name="only-text-template">
		<div class="indent">
			<div class="indent-element">
				<span class="mark">
					<xsl:text>&lt;</xsl:text>
				</span>
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
					<xsl:value-of select="name(.)"/>
					<xsl:if test="@*">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:element>
				<xsl:for-each select="@*">
					<xsl:apply-templates select="."/>
					<xsl:if test="position() != last()">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<span class="mark">
					<xsl:text>&gt;</xsl:text>
				</span>
				<xsl:apply-templates select="node()">
					<xsl:with-param name="indent" select="'no'"/>
				</xsl:apply-templates>
				<span class="mark">&lt;/</span>
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
					<xsl:value-of select="name(.)"/>
				</xsl:element>
				<span class="mark">
					<xsl:text>&gt;</xsl:text>
				</span>
			</div>
		</div>
	</xsl:template>

	<!-- template for elements with element children -->
	<xsl:template match="*[*]" priority="20">
		<div class="indent">
			<div class="indent-element">
				<span class="mark">&lt;</span>
				<xsl:element name="span">
					<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
					<xsl:value-of select="name(.)"/>
					<xsl:if test="@*">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:element>
				<xsl:for-each select="@*">
					<xsl:apply-templates select="."/>
					<xsl:if test="position() != last()">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<span class="mark">
					<xsl:text>&gt;</xsl:text>
				</span>
			</div>
			<div>
				<xsl:apply-templates/>
				<div>
					<span class="mark">
						<xsl:text>&lt;/</xsl:text>
					</span>
					<xsl:element name="span">
						<xsl:attribute name="class"><xsl:if test="xsl:*"><xsl:text>xsl-</xsl:text></xsl:if><xsl:text>element</xsl:text></xsl:attribute>
						<xsl:value-of select="name(.)"/>
					</xsl:element>
					<span class="mark">
						<xsl:text>&gt;</xsl:text>
					</span>
				</div>
			</div>
		</div>
	</xsl:template>

	<!-- template for entity references -->
	<xsl:template name="entity-ref">
		<xsl:param name="name"/>
		<xsl:text>&#38;</xsl:text>
		<xsl:value-of select="$name"/>
		<xsl:text>;</xsl:text>
	</xsl:template>
</xsl:stylesheet>