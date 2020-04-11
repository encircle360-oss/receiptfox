<#-- @ftlvariable name="invoice" type="com.encircle360.oss.receiptfox.model.Invoice" -->
<!DOCTYPE html>
<html>
<head>
    <title>Invoice Template</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        page {
            size: A4;
            margin-left: auto;
            margin-right: auto;
        }

        html {
            height: 100%;
        }

        body {
            margin-left: 2.2cm;
            margin-right: 1cm;
        }

        table {
            border-collapse: collapse;
            border-spacing: 0;
            border: 0;
            width: 100%;
        }

        th, td {
            padding: 0;
        }

        @media print {
            footer {
                page-break-after: always;
            }
        }

        .foldmark {
            position: absolute;
            background-color: black;
            height: 1px;
            width: 3mm;
            left: 4mm;
        }
    </style>
</head>
<body>
<div class="foldmark" style="top:35.35%;"></div>
<div class="foldmark" style="top:70.70%;"></div>
<div style="width:100%; height:5.1cm; background:url(http://encircle360share.s3-eu-central-1.amazonaws.com/test.png) right center no-repeat;"></div>
<div style="font-size:100%; font-family:Helvetica, sans-serif; float:left; width:100%;">
    <table>
        <tr>
            <td>
                <span style="width:70%; font-size:65%; text-decoration:underline;">${invoice.getSender().getCompanyName()} · ${invoice.getSender().getAddressLine1()} · ${invoice.getSender().getPostalCode()} ${invoice.getSender().getCity()}</span><br><br>
                <#if invoice.getReceiver().getCompanyName()??>
                    <strong>${invoice.getReceiver().getCompanyName()}</strong>
                    <br>${invoice.getReceiver().getFirstName()} ${invoice.getReceiver().getLastName()}
                <#else>
                    <strong>${invoice.getReceiver().getFirstName()} ${invoice.getReceiver().getLastName()}</strong>
                </#if>
                <br>${invoice.getReceiver().getAddressLine1()}
                <br><br>${invoice.getReceiver().getPostalCode()} ${invoice.getReceiver().getCity()}
                <br>${invoice.getReceiver().getCountryDisplayName()}
                <#if invoice.getReceiver().getVatId()??>
                    <br><br>VAT ID: ${invoice.getReceiver().getVatId()}<br><br>
                </#if>
                <br><br><br><br><br>
            </td>
            <td style="width:30%; vertical-align:top;">
                <strong>${invoice.getSender().getCompanyName()}</strong><br>${invoice.getSender().getAddressLine1()}
                <br>${invoice.getSender().getPostalCode()} ${invoice.getSender().getCity()}<br> <br>
                <table>
                    <#if invoice.getSender().getPhoneNumber()??>
                        <tr>
                            <td><strong>fon</strong></td>
                            <td>${invoice.getSender().getPhoneNumber()}</td>
                        </tr>
                    </#if>
                    <#if invoice.getSender().getFaxNumber()??>
                        <tr>
                            <td><strong>fax</strong></td>
                            <td>${invoice.getSender().getFaxNumber()}</td>
                        </tr>
                    </#if>
                    <#if invoice.getSender().getEmail()??>
                        <tr>
                            <td><strong>email</strong></td>
                            <td>${invoice.getSender().getEmail()}</td>
                        </tr>
                    </#if>
                    <#if invoice.getSender().getWebsiteUrl()??>
                        <tr>
                            <td><strong>www</strong></td>
                            <td>${invoice.getSender().getWebsiteUrl()}</td>
                        </tr>
                    </#if>
                </table>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td style="font-size: 100%; font-weight: bold;">
                Rechnung RE/12335
            </td>
            <td style="text-align:right; font-size: 90%;">
                ${invoice.getSender().getCity()}, den 02.04.2020
            </td>
        </tr>
        <tr>
            <td colspan="2" style="border-bottom: 1px solid #000;">
                <br>
                KundenNr.: SLY-1234<br>
                Zeitraum: VON-BIS<br>
                <br><br>
            </td>
        </tr>
    </table>

    <table>
        <tr style="font-weight: bold;">
            <td style="width:15%;">Article</td>
            <td style="width:45%;">Description</td>
            <td style="width:10%; text-align:right;">Quantity</td>
            <td style="width:15%; text-align:right;">Unit price</td>
            <td style="width:15%; text-align:right;">Price</td>
        </tr>
        <tr>
            <td colspan="5" style="border-top:1px solid #000;">&nbsp;</td>
        </tr>
        <#list invoice.getItems() as item>
            <tr>
                <td>${item.getName()}</td>
                <td>${item.getDescription()!""}</td>
                <td style="text-align:right;">${item.getCount()}</td>
                <td style="text-align:right;">${item.getNetPrice()} ${invoice.getCurrencySymbol()}</td>
                <td style="text-align:right;">${item.getCount()*item.getNetPrice()} ${invoice.getCurrencySymbol()}</td>
            </tr>
        </#list>
        <tr>
            <td colspan="5" style="border-bottom:1px solid #000;">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:right;">Net amount</td>
            <td style="text-align:right;">${invoice.getTotalNetPrice()} ${invoice.getCurrencySymbol()}</td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:right;">
                VAT<#if invoice.isReverseCharge()> (Reverse Charged)</#if> ${invoice.getVatRate()}</td>
            <td style="text-align:right;">${invoice.getTotalVat()} ${invoice.getCurrencySymbol()}</td>
        </tr>
        <tr>
            <td colspan="5" style="border-bottom: 1px solid #000;">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:right; font-weight:bold;">Invoice amount</td>
            <td style="text-align:right; font-weight:bold;">
                <#if invoice.isReverseCharge()>
                    ${invoice.getTotalNetPrice()} ${invoice.getCurrencySymbol()}
                <#else>
                    ${invoice.getTotalPrice()} ${invoice.getCurrencySymbol()}
                </#if>
            </td>
        </tr>
    </table>

    <br><br><br>

    <table>
        <#if invoice.isReverseCharge()>
            <tr>
                <td>
                    <br>
                    VAT Reverse Charged.
                </td>
            </tr>
        </#if>
        <tr>
            <td style="font-weight:bold;"><u>${invoice.getFooterHeadline()!""}</u></td>
        </tr>
        <tr>
            <td>
                <br>
                ${invoice.getFooterText()!""}
            </td>
        </tr>
    </table>

    <footer style="position:fixed; bottom:5mm; left:0; font-size: 70%; font-weight: bold; text-align: center; width: 100%; vertical-align:bottom;">
        Geschäftsführer ${invoice.getSender().getFirstName()} ${invoice.getSender().getLastName()} · HRB HRBNRHERE ·
        Amtsgericht Köln · VAT ID: ${invoice.getSender().getVatId()}

        <#if invoice.getPayment()??>
            <br>
            ${invoice.getPayment().getBankName()} · IBAN ${invoice.getPayment().getIban()} ·
            BIC ${invoice.getPayment().getBic()}
        </#if>
    </footer>
</div>
</body>
</html>
