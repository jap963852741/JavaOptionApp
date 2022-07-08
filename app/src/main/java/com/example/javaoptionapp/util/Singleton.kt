package com.example.javaoptionapp.util

import okhttp3.OkHttpClient

object Singleton {
    var okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        .addCookieInHeader()
        .addLogIntercept()
        .addCookie()

    var cookie: String =
        "BID=916B474F-CFB5-4FBB-8BF9-0CB8E32E2706; _hjid=4befbb79-c4df-49cc-b107-b125ca36efc6; hblid=sLG01Kgrxd3QpTxQ3h7B70HDF4ao4byb; _gcl_au=1.1.555557533.1633275759; olfsk=olfsk3259789680355396; _okdetect=%7B%22token%22%3A%2216377463278540%22%2C%22proto%22%3A%22about%3A%22%2C%22host%22%3A%22%22%7D; _ok=8391-691-10-7433; _hjSessionUser_827061=eyJpZCI6ImQ2YzZiNGM4LTJkYzUtNTZlZC05OWU3LTcxMTQ4Yzc5NTE4NCIsImNyZWF0ZWQiOjE2Mzc3NDYzMjg1NzksImV4aXN0aW5nIjp0cnVlfQ==; popup=showed; _fbp=fb.1.1637760383878.1083912967; _smt_uid=619e3d80.49eb81dd; BrowserMode=Web; __gads=ID=e90b4921ec01dc20-22d525843ecf003f:T=1633275759:RT=1637770309:S=ALNI_MbWtFHKrWEmErk713siFgyVgGU50A; __cf_bm=.ZoE0obu8noX6JW_fqf0Tkh7gLm.tg4P34ns14uX7z4-1637858560-0-ARDjTwFdzAyrFjl3P760M+csxYFzqPgORRsddlqdPB0Lu7rdse06m9H2fMO5SfBfeNkMWd7V4k9HByOl5VtmaVK3Q3+eKpyGagh7JDDPQYM5cY0ElIX8SYgNAY8aQJ/ufA==; _gid=GA1.2.176892576.1637858561; wcsid=IYQav1eoZois226D3h7B70HX14OaXobb; _okbk=cd4%3Dtrue%2Cvi5%3D0%2Cvi4%3D1637858562363%2Cvi3%3Dactive%2Cvi2%3Dfalse%2Cvi1%3Dfalse%2Ccd8%3Dchat%2Ccd6%3D0%2Ccd5%3Daway%2Ccd3%3Dfalse%2Ccd2%3D0%2Ccd1%3D0%2C; _hjSession_827061=eyJpZCI6IjQzNTI5Y2FiLTBiY2QtNGM5Zi04MGFmLWFhMGY4MzM2ZGUyNiIsImNyZWF0ZWQiOjE2Mzc4NTg1NjMzNDd9; _hjAbsoluteSessionInProgress=0; _hjIncludedInPageviewSample=1; _hjIncludedInSessionSample=0; client_fingerprint=3b3a489e27e7e4437f5659afae6edc6338e9fd02abecaf369e17378ee7dae7fb; _oklv=1637860097826%2CIYQav1eoZois226D3h7B70HX14OaXobb; _gat_UA-6993262-2=1; _ga_FCVGHSWXEQ=GS1.1.1637858560.9.1.1637860107.0; _ga=GA1.2.2057815104.1633275759; _gat_gtag_UA_6993262_2=1"
}