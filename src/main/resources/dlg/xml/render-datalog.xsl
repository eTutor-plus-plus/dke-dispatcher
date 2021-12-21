<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
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
		<xsl:param name="hasSyntaxErrors" select="./datalog-result/@syntax-error = 1 or ./datalog-result/@syntax-error = 'true'"/>
		<xsl:param name="showDetails" select="./datalog-result/@diagnose-level = 3"/>
		<xsl:param name="hideResult" select="./datalog-result/@mode = 'submit'"/>
		<xsl:param name="hasMultipleModels" select="count(*/model) > 1"/>
		<html>
			<head>
				<title>Datalog Result</title>
				<link rel='stylesheet' type='text/css' href='/etutor/css/etutor.css'/>
				<style>
					<xsl:text>&lt;!--</xsl:text>
					<!--						
						*.fact-error {background:#FFFFBD}
						*.fact-error {background:#E1FFE1}						
						*.model-header {background:#FFFFDD}						
						*.indent  {margin-left:2em; text-indent:2em}
						-->
						*.predicate-error {background:#ECF8CC}
						*.fact-error {background:#ECF8CC}
						*.predicate-header {background:#E7E7E7}
						*.model-header {background:#E7E7E7}
						*.toggle-symbol {}
						*.predicate {color:#990000}
						*.indent  {margin-left:2em}						
						*.mark  {color:blue}
					       *.result-box {
							border-width: 4px;
							border-style: double;
							border-color: #d3d3d3;
							padding: 4px; margin: 0em }
						*.dlg_report_error {}
					<xsl:text>--&gt;</xsl:text>
				</style>
				<script type="text/javascript">
					<xsl:text>&lt;!--</xsl:text>
						function toggleSymbol(obj, closed) {
							var symbol = null;
							while (symbol == null <xsl:text>&amp;&amp;</xsl:text> obj != null) {
								if (obj.className == "toggle-symbol") {
									symbol = obj.firstChild;
								} else if (obj.nodeType == 3) {
									obj = obj.nextSibling;
								} else {
									obj = obj.firstChild;
								}
							}
							if(symbol == null) {
								return;
							}
							if (closed) {
								symbol.nodeValue="(+) ";
							} else {
								symbol.nodeValue="(-) ";							
							}
						}
						function toggle(obj) {
							var header = null;
							while (header == null <xsl:text>&amp;&amp;</xsl:text> obj != null) {
								if (obj.className == "predicate-error" || obj.className == "fact-error" || obj.className == "predicate-header"  || obj.className == "model-header") {
									header = obj;
								} else {
									obj = obj.parentElement;
								}
							}
							if(obj == null) {
								return;
							}
							var sibling;
							if(obj.nextSibling.nodeType == 3) {
								sibling=obj.nextSibling.nextSibling;
							} else {
								sibling=obj.nextSibling;
							}

							if(sibling.style.display == 'none') {
								sibling.style.display = 'block';
								toggleSymbol(obj, false);
							} else {
								sibling.style.display = 'none';
								toggleSymbol(obj, true);
							}
						}					
					<xsl:text>--&gt;</xsl:text>
				</script>
			</head>
			<body>
				<table cellpadding="3" cellspacing="1" border="0">
					<tr>
						<td colspan="2">
							<b>
								<xsl:choose>
									<xsl:when test="$hasSyntaxErrors">
										<div class="dlg_report_err">
											<xsl:value-of select="//analysis/summary"/>
										</div>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="//analysis/summary"/>
									</xsl:otherwise>
								</xsl:choose>
							</b>
						</td>
					</tr>
					<xsl:if test="string-length(normalize-space(//analysis/grading)) > 0">
						<tr>
							<td colspan="2">
								<xsl:value-of select="//analysis/grading"/>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="string-length(normalize-space(//analysis/syntax)) > 0">
						<tr>
							<td colspan="2">
								<div class="dlg_report_err">
									<xsl:value-of select="//analysis/syntax"/>
								</div>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="not($hideResult)">
						<xsl:if test="string-length(normalize-space(//analysis/consistency)) > 0">
							<tr>
								<td colspan="2">
									<xsl:value-of select="//analysis/consistency"/>
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td/>
							<td/>
						</tr>
						<xsl:if test="count(//analysis/error) > 0 and not($showDetails)">
							<tr>
								<td colspan="2">
									<xsl:for-each select="//analysis/error">
										<li>
											<xsl:value-of select="."/>
										</li>
									</xsl:for-each>
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td/>
							<td/>
						</tr>
						<xsl:if test="not($hasSyntaxErrors)">
							<tr>
								<td/>
								<td/>
							</tr>
							<tr>
								<td/>
								<td/>
							</tr>
							<tr>
								<td colspan="2">
									<i>This is the result of your query:</i>
								</td>
							</tr>
							<tr>
								<td colspan="2" class="result-box">
									<xsl:if test="count(//predicate) = 0">
										&#160;&#160;&#160;
									</xsl:if>							
									<xsl:for-each select="*/model">
										<xsl:choose>
											<xsl:when test="$hasMultipleModels">
												<div class="model-header" onclick="toggle(this);" style="cursor:pointer">
													<p>
														<b class="toggle-symbol">(-) </b>
														<i>
															<xsl:value-of select="position()"/>. Model:</i>
													</p>
												</div>
												<div class="indent">
													<xsl:apply-templates select="."/>
												</div>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates select="."/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</td>
							</tr>
						</xsl:if>
					</xsl:if>						
				</table>
			</body>
		</html>
	</xsl:template>
	
	<!-- template for the node which represents the whole datalog result -->
	<xsl:template match="model">
		<!-- code for error categories -->
		<xsl:param name="missingPredicate" select="'missing-predicate'"/>
		<xsl:param name="redundantPredicate" select="'redundant-predicate'"/>
		<xsl:param name="lowArity" select="'low-term-predicate'"/>
		<xsl:param name="highArity" select="'high-term-predicate'"/>
		<!-- messages according to the code for error categories -->
		<xsl:param name="msgMissingPredicates" select="//analysis/error[@code=$missingPredicate]"/>
		<xsl:param name="msgRedundantPredicates" select="//analysis/error[@code=$redundantPredicate]"/>
		<xsl:param name="msgLowArity" select="//analysis/error[@code=$lowArity]"/>
		<xsl:param name="msgHighArity" select="//analysis/error[@code=$highArity]"/>
		<xsl:for-each select="predicate[not(@error)]">
			<xsl:apply-templates select="."/>
		</xsl:for-each>
		<xsl:if test="count(predicate[@error=$lowArity]) > 0">
			<div class="predicate-error" onclick="toggle(this);" style="cursor:pointer">
				<p>
					<b class="toggle-symbol">(-) </b>
					<i>
						<xsl:value-of select="$msgLowArity"/>
					</i>
				</p>
			</div>				
			<div class="indent">
				<xsl:for-each select="predicate[@error=$lowArity]">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</div>
		</xsl:if>
		<xsl:if test="count(predicate[@error=$highArity]) > 0">
			<div class="predicate-error" onclick="toggle(this);" style="cursor:pointer">
				<p>
					<b class="toggle-symbol">(-) </b>
					<i>
						<xsl:value-of select="$msgHighArity"/>
					</i>
				</p>
			</div>				
			<div class="indent">
				<xsl:for-each select="predicate[@error=$highArity]">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</div>
		</xsl:if>
		<xsl:if test="count(predicate[@error=$redundantPredicate]) > 0">
			<div class="predicate-error" onclick="toggle(this);" style="cursor:pointer">
				<p>
					<b class="toggle-symbol">(-) </b>
					<i>
						<xsl:value-of select="$msgRedundantPredicates"/>
					</i>
				</p>
			</div>				
			<div class="indent">
				<xsl:for-each select="predicate[@error=$redundantPredicate]">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</div>
		</xsl:if>
		<xsl:if test="count(predicate[@error=$missingPredicate]) > 0">
			<div class="predicate-error" onclick="toggle(this);" style="cursor:pointer">
				<p>
					<b class="toggle-symbol">(-) </b>
					<i>
						<xsl:value-of select="$msgMissingPredicates"/>
					</i>
				</p>
			</div>
			<div class="indent">
				<xsl:for-each select="predicate[@error=$missingPredicate]">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!-- the 'filter' option allows to specify which predicates should be output -->
	<xsl:template match="predicate[@filter = 'false' or @filter = '0']" priority="20"/>
	
	<!-- general template for outputting predicates of the datalog result -->
	<xsl:template match="predicate" name="predicate">
		<xsl:param name="name" select="@name"/>
		<!-- code for error categories -->
		<xsl:param name="missingFact" select="'missing-fact'"/>
		<xsl:param name="redundantFact" select="'redundant-fact'"/>
		<xsl:param name="negativeFact" select="'negative-fact'"/>
		<xsl:param name="positiveFact" select="'positive-fact'"/>
		<!-- messages according to the code for error categories -->
		<xsl:param name="msgMissingFacts" select="//analysis/error[@code=$missingFact]"/>
		<xsl:param name="msgRedundantFacts" select="//analysis/error[@code=$redundantFact]"/>
		<xsl:param name="msgNegativeFacts" select="//analysis/error[@code=$negativeFact]"/>
		<xsl:param name="msgPositiveFacts" select="//analysis/error[@code=$positiveFact]"/>
		<div class="predicate-header" onclick="toggle(this);" style="cursor:pointer">
			<p>
				<b class="toggle-symbol">(-) </b>
				<i>
					<xsl:value-of select="$name"/>
				</i>
			</p>
		</div>
		<div class="indent">
			<xsl:for-each select="fact[not(@error)]">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
			<xsl:if test="count(fact[@error=$negativeFact]) > 0">
				<div class="fact-error" onclick="toggle(this);" style="cursor:pointer">
					<p>
						<b class="toggle-symbol">(-) </b>
						<i>
							<xsl:value-of select="$msgNegativeFacts"/>
						</i>
					</p>
				</div>
				<div class="indent">
					<xsl:for-each select="fact[@error=$negativeFact]">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</div>
			</xsl:if>
			<xsl:if test="count(fact[@error=$positiveFact]) > 0">
				<div class="fact-error" onclick="toggle(this);" style="cursor:pointer">
					<p>
						<b class="toggle-symbol">(-) </b>
						<i>
							<xsl:value-of select="$msgPositiveFacts"/>
						</i>
					</p>
				</div>
				<div class="indent">
					<xsl:for-each select="fact[@error=$positiveFact]">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</div>
			</xsl:if>			
			<xsl:if test="count(fact[@error=$redundantFact]) > 0">
				<div class="fact-error" onclick="toggle(this);" style="cursor:pointer">			
					<p>
						<b class="toggle-symbol">(-) </b>
						<i>
							<xsl:value-of select="$msgRedundantFacts"/>
						</i>
					</p>
				</div>
				<div class="indent">
					<xsl:for-each select="fact[@error=$redundantFact]">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</div>
			</xsl:if>
			<xsl:if test="count(fact[@error=$missingFact]) > 0">
				<div class="fact-error" onclick="toggle(this);" style="cursor:pointer">
					<p>
						<b class="toggle-symbol">(-) </b>
						<i>
							<xsl:value-of select="$msgMissingFacts"/>
						</i>
					</p>
				</div>					
				<div class="indent">
					<xsl:for-each select="fact[@error=$missingFact]">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<!-- general template for outputting facts of the datalog result -->
	<xsl:template match="fact" name="fact">
		<div>
			<xsl:if test="@negated='true' or @negated = '1' or @negated = 1">
				<xsl:text>-</xsl:text>
			</xsl:if>
			<span class="predicate">
				<xsl:value-of select="../@name"/>
			</span>
			<xsl:for-each select="term">
				<xsl:if test="position() = 1">
					<span class="mark">
						<xsl:text>(</xsl:text>
					</span>
				</xsl:if>
				<b>
					<xsl:value-of select="@value"/>
				</b>
				<xsl:if test="position() != last()">
					<span class="mark">
						<xsl:text>, </xsl:text>
					</span>
				</xsl:if>
				<xsl:if test="position() = last()">
					<span class="mark">
						<xsl:text>)</xsl:text>
					</span>
				</xsl:if>
			</xsl:for-each>
			<xsl:text>.</xsl:text>
		</div>
	</xsl:template>
</xsl:stylesheet>