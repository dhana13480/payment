ags = "RMCS"
region = "east"

tags {
    ags = "RMCS"
    costCenter = "RSM302"
    sdlc = "DEV"
    owner = "Sachin Goel"
}

deliveryStream {
    enableHttpStream = true
}

environments {
    qa {
        tags {
            sdlc = "QA"
        }
    }

    qa02 {
        tags {
            sdlc = "QA02"
        }
    }

    qaint02 {
        tags {
            sdlc = "QAINT02"
        }
    }

    uat {
        tags {
            sdlc = "uat"
        }
    }

    prod {
        tags {
            sdlc = "PROD"
        }
    }
}
