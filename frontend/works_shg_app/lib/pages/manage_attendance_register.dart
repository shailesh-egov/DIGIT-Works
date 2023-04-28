import 'package:digit_components/digit_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:works_shg_app/utils/Constants/i18_key_constants.dart' as i18;
import 'package:works_shg_app/utils/notifiers.dart';
import 'package:works_shg_app/widgets/WorkDetailsCard.dart';

import '../blocs/attendance/search_projects/search_projects.dart';
import '../blocs/localization/app_localization.dart';
import '../models/attendance/attendance_registry_model.dart';
import '../utils/common_methods.dart';
import '../widgets/Back.dart';
import '../widgets/SideBar.dart';
import '../widgets/atoms/app_bar_logo.dart';
import '../widgets/atoms/empty_image.dart';
import '../widgets/drawer_wrapper.dart';
import '../widgets/loaders.dart' as shg_loader;

class ManageAttendanceRegisterPage extends StatelessWidget {
  const ManageAttendanceRegisterPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          titleSpacing: 0,
          title: const AppBarLogo(),
        ),
        drawer: DrawerWrapper(Drawer(
            child: SideBar(
          module: CommonMethods.getLocaleModules(),
        ))),
        body: SingleChildScrollView(child: BlocBuilder<
            AttendanceProjectsSearchBloc,
            AttendanceProjectsSearchState>(builder: (context, state) {
          var localization = AppLocalizations.of(context);
          return state.maybeWhen(
              loading: () => shg_loader.Loaders.circularLoader(context),
              loaded: (AttendanceRegistersModel? attendanceRegistersModel) {
                final attendanceRegisters = List<AttendanceRegister>.from(
                    attendanceRegistersModel!.attendanceRegister!);

                attendanceRegisters.sort((a, b) =>
                    b.registerAuditDetails!.lastModifiedTime!.compareTo(
                        a.registerAuditDetails!.lastModifiedTime!.toInt()));

                final projectList = attendanceRegisters
                    .map((e) => {
                          i18.workOrder.workOrderNo: e
                                  .attendanceRegisterAdditionalDetails
                                  ?.contractId ??
                              'NA',
                          i18.attendanceMgmt.registerId: e.registerNumber,
                          i18.attendanceMgmt.projectId: e
                                  .attendanceRegisterAdditionalDetails
                                  ?.projectId ??
                              'NA',
                          i18.attendanceMgmt.projectDesc: e
                                  .attendanceRegisterAdditionalDetails
                                  ?.projectName ??
                              'NA'
                        })
                    .toList();

                return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Back(
                        backLabel: AppLocalizations.of(context)
                            .translate(i18.common.back),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Text(
                          '${AppLocalizations.of(context).translate(i18.attendanceMgmt.attendanceRegisters)}(${attendanceRegistersModel.attendanceRegister!.length})',
                          style: DigitTheme
                              .instance.mobileTheme.textTheme.displayMedium
                              ?.apply(color: const DigitColors().black),
                          textAlign: TextAlign.left,
                        ),
                      ),
                      projectList.isEmpty
                          ? EmptyImage(
                              align: Alignment.center,
                              label: localization.translate(
                                i18.attendanceMgmt.noRegistersFound,
                              ))
                          : WorkDetailsCard(
                              projectList,
                              isManageAttendance: true,
                              elevatedButtonLabel: AppLocalizations.of(context)
                                  .translate(
                                      i18.attendanceMgmt.enrollWageSeeker),
                              attendanceRegistersModel: attendanceRegisters,
                            )
                    ]);
              },
              error: (String? error) =>
                  Notifiers.getToastMessage(context, error.toString(), 'ERROR'),
              orElse: () => Container());
        })));
  }
}