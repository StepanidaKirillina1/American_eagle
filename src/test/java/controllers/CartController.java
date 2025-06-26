package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.CartPayloadData;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class CartController {
    RequestSpecification requestSpecification;
    TokenController tokenController = new TokenController();
    private static final String CART_ENDPOINT = "bag/v1";

    public CartController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                //.header("x-access-token", tokenController.getToken())
                .header("Authorization", "Bearer " + tokenController.getToken())
                //.header("Cookie", "TLTUID=EF40B7393D493635E2824E89BD9FAFB0; aeoStyliticsSegment=false; optimizelyEndUserId=cd26e4c5-94d2-4bc4-8aab-b7fde81f7b7c; aeoUserLocation=RU:; ae_clarip_consent=0,1,2,3; ae_clarip_ab_segment=true; brand=aeo; tkbl_session=22ae2cfc-834a-4aef-9542-044e1a297fba; ConstructorioID_client_id=2f495668-7634-4e40-8b56-3a1ea504d772; swim_ten=f; user_profile_id=undefined; _ga=GA1.1.229799860.1750766922; QuantumMetricUserID=0d3c6d48673bff59b027b9b2a2fe2d4c; cjConsent=MHxOfDB8Tnww; cjUser=76f65c32-e7c2-40c2-b1d9-85560d1aaa4d; _gcl_au=1.1.178387464.1750766925; _scid=m-sW40T4juQgL_avja9aBozw0mLyDQh1; __exponea_etc__=ae2593f8-b326-48bc-89fc-626f343d48df; _ScCbts=%5B%5D; _sctr=1%7C1750690800000; _fbp=fb.1.1750766927747.47369547493107241; _tt_enable_cookie=1; _ttp=01JYGXEH2H24E6CV84MHEAZW2P_.tt.1; _gegeo=JTdCJTIyY291bnRyeSUyMiUzQSUyMlJ1c3NpYSUyMiUyQyUyMmNvdW50cnlDb2RlJTIyJTNBJTIyUlUlMjIlMkMlMjJob3N0aW5nJTIyJTNBZmFsc2UlMkMlMjJtb2JpbGUlMjIlM0FmYWxzZSUyQyUyMnByb3h5JTIyJTNBZmFsc2UlMkMlMjJzdGF0dXMlMjIlM0ElMjJzdWNjZXNzJTIyJTdE; geuid=114520b5-d598-4971-9d76-69bb738db3b4; _gepi=true; _geps=true; _lc2_fpi=564cdb68d47c--01jygxnzcanpytdz54hr85x7bd; _pin_unauth=dWlkPVptVmtORGhrT0dVdE9ERTNaUzAwTVdFMkxXRXhOREl0TkRJNU1UbGtaVGd4TVRreQ; _geffran=3; BVBRANDID=1ed2bb99-3ab8-42c7-90a9-02994cceaf28; fita.sid.americaneagle=ggbYOtP3oqGz0dHKqVII0Mmycgw6-EBC; ae_i18n=en|US|USD|US|us; ae_lang=en; akaalb_PROD_ALB=~op=PROD_LB_ALL_DCs:PROD_LB_Origin_East4_AGWA_Service_Mesh|~rv=64~m=PROD_LB_Origin_East4_AGWA_Service_Mesh:0|~os=db0ecb8ead961a2983478ea211c488b6~id=424fd478f44a5d92488e3b54c509d4f0; akaalb_PROD_ALB_API=~op=PROD_LB_APICG_EAST5:PROD_LB_Origin_APICG_East5|~rv=47~m=PROD_LB_Origin_APICG_East5:0|~os=db0ecb8ead961a2983478ea211c488b6~id=164fdaecf444c39825509517f924e59d; QuantumMetricSessionID=1bf97617ee658ef3378e36da1511f450; acquisition_value=; acquisition_location=https://www.ae.com/us/en; acquisition_location_gtm=https://www.ae.com/us/en; acquisition_value_gtm=; _li_dcdm_c=.ae.com; _lc2_fpi_js=564cdb68d47c--01jygxnzcanpytdz54hr85x7bd; optimizelySession=0; granify.uuid=10b592f1-9ca3-4474-99f1-ca999f5bd601; granify.new_user.sxkhw=false; granify.session.sxkhw=-1; bm_mi=5118FDA29B6A45D1D5AF14F379D4937B~YAAQUHItF7p+d4CXAQAAZ9IppBxhqiUUbdswMD5xw94040uQKEJo3jqebzuNURRmpaq9i2w7H/ndIvAH0NrljwwYq+KD6lWyHWURFDYjvjj4F0Ao8oVpgwn3IpITS80eGBQJS7Pkcy1fSRMD91VqWVQn1vN8ES5CMNwEfEoE7rBxBhzfZ1yFf13IjiI12V73egdgda1tjrUHEynU/kZQh7eh2Xqr44GwewbvE9iAeivCSTBYwbgQLP9LyX/6rhcjPe5sKtkEMnydOeRKtQHGAEWcHtmDSQqGjyx8mOo61vn13v+LmEenqjeqHsMC8b0=~1; ak_bmsc=2F3B06DB2E824070234770F55CEA0E49~000000000000000000000000000000~YAAQUHItF6l/d4CXAQAArNwppBzYgeM3WGGJz9C8UFKP0Rfgb3lmxm6nJ+lOMz/NWTlnQxdFMdWl4m5pVCY+Qnqd5+WeEyLz5JpdrQU2znbDuZ/HF5UIU2107JmG+8zeIvQXSHIshO8psdjI+aJQ0rMBfQF+2d2oL65Iv1+hXFg+eemYMmxe5Tbk/BQtxOy5f09XHuKV9pWXDz44NVnWmS45V/znHaccevECbKGu0LEwhVP3rX9dYkAv+zBuM7PWwonHDmEEU62dIja+qxAdpOdPNnAzyh700EeVyUvZftpKs+qTpUD4JKmxI0nQ45E25+QOrJ3vzUy5iUQjunqAz1dxe1JlTR9U1Uji6ulufrEK+nXc65hzUfKFUpTIpTzOEKqXSu3F0Z/X8+SqqY+6xXoR+Nv3+jHxcG2lYDjkShO+UGI2pgB9Pkgof942RwEPeJiZV1E6k4zoJE9fMm8ucOVlFzug2MshkVPL+PInoUpn; ConstructorioID_session_id=3; __exponea_time2__=104.57368898391724; _gecntaos=MQ==; _geaos=true; _getdran=4; aeoUserIp=109.195.118.245; bm_sz=ECD73956306850F0438F754DA3D7914A~YAAQmQcQArhpKHCXAQAAJIE5pBybu4fUwwIzkZOvv25zfDS54+CF0TRaYhqODFiHRwzgm3yuh3F6fEFrufmrK+LkHxRbHbm2hTtEzqG7Y770O+us/z1VYD32lYXRJZx0EuX4t9ASP/HAU1q8HWzBF2mAdUCx9uUsC+CZ1EorrEGeMvRwTvx16rTebAiDbAjSWYd+pQI4s0H/6bgmN65tBK/dxkOHIVC7fP0gKyu8sfT8DFr/8o1tA/mYHLIJ96VRDOvOjRkW/5xbI3+71yzz7kYPiHsy0sg/W7inG9IKMKsLA1NhgJOLE0guIyB2kEPGRCPh55Wl/CFo+WAMLzrNdDyHQ7KZn1LHQBdNcyc57Vhr1YsN7UfRGhR09G9Dlzho5G5/tRqSP6M4/wbLXyIs3NyLkWjpGAhkv9ZULuIHxxADG1piaf8LInqQC/7j1BGttjQVbivnVybmSO0PMl04JW86qXbesNc=~3752517~4408899; ConstructorioID_session={\"sessionId\":3,\"lastTime\":1750806818851}; __rtbh.lid=%7B%22eventType%22%3A%22lid%22%2C%22id%22%3A%22QnP18uTOkk9JP8zRwC6o%22%2C%22expiryDate%22%3A%222026-06-24T23%3A13%3A39.311Z%22%7D; _br_uid_2=uid%3D9711808791947%3Av%3D17.0%3Ats%3D1750766926598%3Ahc%3D11; _scid_r=rWsW40T4juQgL_avja9aBozw0mLyDQh1SJdnTg; aeoUserJourney=journey-user-scrolled%2Cjourney-users-who-added-item-to-bag%2Cjourney-quickview_launch-example2; ttcsid=1750805796398::d9z3k0CcjFd1JAcy_PPf.3.1750807235096; __rtbh.uid=%7B%22eventType%22%3A%22uid%22%2C%22id%22%3A%22%22%2C%22expiryDate%22%3A%222026-06-24T23%3A20%3A35.099Z%22%7D; ttcsid_C0HVR0KP76SVVJ0UU9SG=1750805796397::UAmjmLgqqqHM5WlrKGu3.3.1750807235272; _abck=E6547C5D827B72684B6C265F5CDDE238~-1~YAAQmQcQAkJaKXCXAQAAt+0/pA5iu0rZ/jp+gdi1H8SUj/IutIKLqJFpoXpeUaOX+NsqsbiT7C1EUs2bMFq72AHYj8LT55ooS5SvCNrao4DWZ9cBg106BscFDO/nReY232UixoLdDylTVmGXf7PbSHaM0fkUFgV0w12N7EB3ifk8yLNBvr//5zf8JBqYURKtRsG2/RloEmx2MpZNCewOHl1bTkbmIeSCju5Q1phfjvDqZPEzGQbzcIZA5h70PQxPP8UI6rQ8XyPv8oDb3FmcnMh26nWicoLbUiV67fREMSRMtJ/UUjUqa6DZMXZrGBz4ePDlR2Xtydeu8rX4mkGNba+y3IvjCVYVgqX6NBlkQ6yEzsWRng5UpZ9GdhsumiINMATIxaWe4bY+NWepVGiENB6vaDUYfT4SrpeiD3jfOORzAvexpZZsGa13ijkxR0HuX9KjWfrYGc4E62+1hqwfn+8q58FSGWsNpsLvSyP67uCRWaRZT+R5hAOmqALQ9AWiRIq8ATOccaoHna7YnI0odjFV+0A3+UhFIzRPppyuw5auYFlPo84noo7NbEGFyvtbq83uJlB4gQYpmgDbxVAvgFRDQvKKoXT+z1FQziC9ytSNtAETitsbW0rczJZtz9x/t9yrraiAVkoBIW7wGPCdA1dF2/S/c0JrZQF/QkWzj4aXu3SqHJguN6ehk7FeXH7CNX7hPzzXz/UOzPQm8Dm5idNy/ZO+o4UUceNdumOHXoN7l3S5dag6N8EZLUXPdKU4S6Wx+vQS8weFd0bs3NLFYRT5C6FB9crKZKIX0PRGCyWiH2NMJ5iYbU+CILpQL0gnKVh9TYeLdsU2YcTYhNnXx+JtNspDbu6Tg6ux+NIoEgcthkkzG+uH8HZVU0/29Y8pp+xlADuqZAKP/UnA4pjSvyLD9YLSsFD4p6FwE2UK7+tvkexddR+k6GwVhh51+WKwVNQh79xSnOaTIBNuLRYLq5U5LmrbCMWxydDSW7PG2r9SOp1O+Z401yZy1apSyct5Jmi0~0~-1~-1; bm_sv=7D59CFA75D1BD64462F6CB930242CCBB~YAAQnQcQAq3MaXuXAQAA/O4/pBxkuh0tPrgHMb7ZtRFPTeHJ/V/ShPFRjS9eRyQT9ReBDANDZUM/mMqYRJcDd9Z6ExF93wwUxfE8N7BmiUPGfNUCevWkP2cP8ok0GizndxB/ErQmoFnf11IR1eXu1y8IEqxEl9fPvod6wUVTMDjpoYz138111CuGFOAZRlox/R6tWa9mEdJbQ0DM26eytF7UYjCRcPDWhPsSoWWvyjdzKTRga+htKcmySrAU~1; utag_main=v_id:0197a1d724c90020ccf13a58af920506f001806700887$_sn:3$_se:15$_ss:0$_st:1750809039435$dc_visit:3$_prevpage:AE%3Ajeans%3Ahome%3Bexp-1750810839436$ses_id:1750805793054%3Bexp-session$_pn:3%3Bexp-session$dc_event:5%3Bexp-session$dc_region:ap-northeast-1%3Bexp-session; _ga_XGBGNYD4S1=GS2.1.s1750805794$o3$g1$t1750807239$j42$l0$h1497707854")
                .baseUri(API_BASE_URL);
    }

    public Response addItemToCart(CartPayloadData items) {
        return given(requestSpecification)
                .body(items)
                .when()
                .post(CART_ENDPOINT + "/items")
                .then()
                .extract().response();
    }

    public Response getCartItemsCount() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "/count")
                .then()
                .extract().response();
    }

    public Response getCartData() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "?couponErrorBehavior=cart&inventoryCheck=true")
                .then()
                .extract().response();
    }
}
