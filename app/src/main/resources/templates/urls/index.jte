@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.Util
@param UrlsPage page
@template.layout.page(
page = page,
content = @`
<section>
    <div class="container-lg mt-5">
        <h1>Сайты</h1>
        <table class="table table-bordered table-hover mt-3">
            <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
            </thead>
            <tbody>
            @for(var url : page.getUrls())
                <tr>
                    <td>
                        ${url.getId()}
                    </td>
                    <td>
                        <a href="${NamedRoutes.urlDetailPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <td>
                        @if(url.getLastCheckStatusCode() != null && url.getLastCheckStatusCode() != 0)
                            ${url.getLastCheckStatusCode()}
                        @endif
                    </td>
                    <td>
                        @if(url.getLastCheckTime() != null)
                            ${Util.formatTimestamp(url.getLastCheckTime())}
                        @endif
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
</section>
`)