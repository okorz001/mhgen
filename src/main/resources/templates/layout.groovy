modelTypes = {
    boolean pjax
    String title
}

if (pjax) {
    content()
}
else {
    yieldUnescaped '<!DOCTYPE html>'
    html {
        head {
            meta(charset: 'UTF-8')
            meta(name: 'viewport',
                 content: 'width=device-width, initial-scale=1')
            link(href: '/assets/style.css', rel: 'stylesheet')
            title(title)
        }
        body {
            content()
        }
    }
}