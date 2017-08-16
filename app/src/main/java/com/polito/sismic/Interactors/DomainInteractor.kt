package com.polito.sismic.Interactors

import com.polito.sismic.Domain.ReportSection

//Bridge between UI and Domain/Database
class DomainInteractor(val mReportManager: ReportManager) {

    fun  addDomainReportSection(sectionParameters: ReportSection) {
        mReportManager.addSectionParameters(sectionParameters)
    }


}