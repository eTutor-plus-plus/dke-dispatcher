<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:error="http://www.dke.jku.at" xml:space="default">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="no"/>

	<!-- Template for adapting the xml output of the transformed XSL stylesheet. -->
	<xsl:template match="xsl:output">
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:if test="name() != 'indent' and name() != 'method' and name() != 'omit-xml-declaration'">
					<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
				</xsl:if>
			</xsl:for-each>
			<xsl:attribute name="indent">yes</xsl:attribute>
			<xsl:attribute name="method">xml</xsl:attribute>
			<xsl:attribute name="omit-xml-declaration">no</xsl:attribute>
		</xsl:element>
	</xsl:template>
	
	<!-- Most common template -->
	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- Template for 'xsl:element' nodes in templates of the transformed XSL stylesheet -->
	<xsl:template match="/*/xsl:template[@match]//xsl:element[@name]">
		<xsl:param name="matchValue" select="../@match"/>
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:for-each select="node()">
				<xsl:choose>
					<!-- An 'xsl:attribute' element within the 'xsl:element' indicates, that an attribute is missing. -->
					<xsl:when test="self::xsl:attribute">
						<xsl:text disable-output-escaping="yes">&lt;xsl:attribute name="error:</xsl:text>
						<xsl:value-of select="@name"/>
						<xsl:text disable-output-escaping="yes">" namespace="http://www.dke.jku.at"&gt;missing&lt;/xsl:attribute&gt;
						</xsl:text>
						<xsl:apply-templates select="."/>
					</xsl:when>
					<!-- An 'xsl:apply-templates' element without 'select' attribute must be supplemented with this 'select' attribute. -->
					<xsl:when test="self::xsl:apply-templates[not(@select)]">
						<xsl:element name="{name()}">
							<xsl:attribute name="select">node()|@*</xsl:attribute>
							<xsl:apply-templates select="@*|node()"/>
						</xsl:element>
					</xsl:when>
					<!-- Every element which is an XSL element may indicate an error, which is handled within this stylesheet, if
					the element matches some template. -->
					<xsl:when test="self::xsl:*">
						<xsl:apply-templates select="."/>
					</xsl:when>
					<!-- Every node other than XSL nodes represents a node which is missing in the result. -->
					<xsl:otherwise>
						<xsl:choose>
							<!-- Initial text nodes are overwritten in the result of the diff tool, get the inital text node again -->
							<xsl:when test="self::text()">
								<error:node error="incorrect-value">
									<xsl:text disable-output-escaping="yes">&lt;xsl:value-of select="</xsl:text>
									<xsl:value-of select="$matchValue"/>
									<xsl:text disable-output-escaping="yes">"/&gt;</xsl:text>
								</error:node>
							</xsl:when>
							<xsl:otherwise>
								<error:node error="missing">
									<xsl:apply-templates select="."/>
								</error:node>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	
	<!-- Template for templates (as stupid as it might look) -->
	<xsl:template match="/*/xsl:template[@match]">
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:choose>
				<!-- If there is an 'xsl:copy-of' element anywhere else in the transformed template with the same 'match' value, 
				this indicates that the current node would be copied to this location. This means that the node is displaced.
				The transformed stylesheet is adapted so that the node is not copied but remains where it is found, but
				it is marked as a displaced node. The corresponding 'xs:copy-of' element must be removed, which is done in another
				template of this stylesheet. -->
				<xsl:when test="@match = //xsl:copy-of/@select">
					<xsl:text disable-output-escaping="yes">
						&lt;xsl:element name="error:node" namespace="http://www.dke.jku.at"&gt;
							&lt;xsl:attribute name="error"&gt;displaced&lt;/xsl:attribute&gt;
							&lt;xsl:copy-of select="."/&gt;
						&lt;/xsl:element&gt;
					</xsl:text>
				</xsl:when>
				<!-- This is another case of a node which is detected to be displaced and must be marked as displaced instead of
				being copied. This is true if there is an 'xsl:element' element anywhere else in the transformed template which refers to the 'match'
				value of the currently transformed template. The corresponding 'xs:element' element must be removed, which once more is done in 
				another template of this stylesheet. -->
				<xsl:when test="concat('{name(', @match, ')}') = //xsl:element/@name">
					<xsl:text disable-output-escaping="yes">
						&lt;xsl:element name="error:node" namespace="http://www.dke.jku.at"&gt;
							&lt;xsl:attribute name="error"&gt;displaced&lt;/xsl:attribute&gt;
							&lt;xsl:copy-of select="."/&gt;
						&lt;/xsl:element&gt;
					</xsl:text>
				</xsl:when>				
				<!-- If the template contains an 'xsl:attribute' element, this indicates that the node which matches the template, is
				an attribute of the result, which has a wrong value.-->
				<xsl:when test="xsl:attribute">
					<xsl:text disable-output-escaping="yes">
						&lt;xsl:attribute name="error:{name()}" namespace="http://www.dke.jku.at"&gt;incorrect-value&lt;/xsl:attribute&gt;
						&lt;xsl:copy-of select="."/&gt;
					</xsl:text>
				</xsl:when>
				<!-- If the template contains no XSL element which copies the current node (the node which matches the template) 
				somehow, this node will be redundant most propably. -->
				<xsl:when test="not(xsl:copy/xsl:apply-templates/@select='node()|@*') and not(xsl:element/@name='{name()}')">
					<!-- An attribute must be distinguished from everything else but an attribute, because the error of these two
					groups is represented differently -->
					<xsl:text disable-output-escaping="yes">
						&lt;xsl:choose&gt;
							&lt;xsl:when test="self::* or self::text() or self::processing-instruction()"&gt;
								&lt;xsl:element name="error:node" namespace="http://www.dke.jku.at"&gt;
									&lt;xsl:attribute name="error"&gt;redundant&lt;/xsl:attribute&gt;
									&lt;xsl:copy-of select="."/&gt;
								&lt;/xsl:element&gt;
					</xsl:text>
					<xsl:if test="node()">
						<error:node error="missing">
							<xsl:apply-templates select="node()"/>
						</error:node>
					</xsl:if>
					<xsl:text disable-output-escaping="yes">
							&lt;/xsl:when&gt;
							&lt;xsl:otherwise&gt;
								&lt;xsl:attribute name="error:{name()}" namespace="http://www.dke.jku.at"&gt;redundant&lt;/xsl:attribute&gt;
								&lt;xsl:copy-of select="."/&gt;
							&lt;/xsl:otherwise&gt;
						&lt;/xsl:choose&gt;
					</xsl:text>
				</xsl:when>
				<!-- Left choice: the current node is copied, possibly changed (for example if it is an 'xsl:element' node), possibly added with 
				missing nodes before or after -->
				<xsl:otherwise>
					<xsl:for-each select="node()">
						<xsl:choose>
							<!-- Empty text nodes must be ignored -->
							<xsl:when test="self::text() and normalize-space(.) = ''"/>
							<xsl:when test="namespace-uri(.) != 'http://www.w3.org/1999/XSL/Transform' or self::xsl:processing-instruction">
								<xsl:element name="error:node">
									<xsl:attribute name="error">missing</xsl:attribute>
									<xsl:apply-templates select="."/>
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>

	<!-- Template for adapting templates so that displaced node are not copied to the correct location and remain where they are found;
	The 'xsl:copy-of' element will be enclosed in a comment. -->
	<xsl:template match="/*/xsl:template[@match]/xsl:copy-of[@select]" xml:space="default">
		<xsl:call-template name="ignore-element"/>
	</xsl:template>

	<!-- Template for adapting templates so that displaced node are not copied to the correct location and remain where they are found;
	The 'xsl:element' element will be enclosed in a comment. This template matches an element if the following applies: 
	An 'xsl:element' element contains an 'xsl:value-of' element and there is a certain connection between the 'name' attribute of the first 
	element and the 'select' attribute of the latter one. -->
	<xsl:template match="/*/xsl:template[@match]//xsl:element[xsl:value-of/@select and @name = concat('{name(', xsl:value-of/@select, ')}')]" xml:space="default">
		<xsl:call-template name="ignore-element"/>
	</xsl:template>
	
	<!-- Template for enclosing nodes in a comment -->
	<xsl:template name="ignore-element">
		<xsl:text disable-output-escaping="yes">&lt;!--</xsl:text>
		<xsl:copy-of select="."/>
		<xsl:text disable-output-escaping="yes">--&gt;</xsl:text>
	</xsl:template>
</xsl:stylesheet>