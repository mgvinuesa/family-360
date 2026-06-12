package dev.mgvinuesa.family360.family.api;

import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.api.v1.model.Family;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMember;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberPage;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberType;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyPage;
import java.util.List;

final class FamilyApiMapper {

    private FamilyApiMapper() {
    }

    static Family toApi(dev.mgvinuesa.family360.family.domain.Family family) {
        Family result = new Family(family.id(), family.createdAt())
                .name(family.name())
                .currency(family.currency());
        if (family.disabledAt() != null) {
            result.disabledAt(family.disabledAt());
        }
        return result;
    }

    static FamilyPage toFamilyPage(PageResult<dev.mgvinuesa.family360.family.domain.Family> page) {
        List<Family> data = page.data().stream().map(FamilyApiMapper::toApi).toList();
        return new FamilyPage(
                page.page(),
                page.limit(),
                Math.toIntExact(page.totalElements()),
                page.totalPages(),
                data
        );
    }

    static FamilyMember toApi(dev.mgvinuesa.family360.family.domain.FamilyMember member) {
        FamilyMember result = new FamilyMember(member.id(), member.familyId(), member.createdAt())
                .name(member.name())
                .memberType(FamilyMemberType.valueOf(member.memberType().name()))
                .birthDate(member.birthDate());
        if (member.disabledAt() != null) {
            result.disabledAt(member.disabledAt());
        }
        return result;
    }

    static FamilyMemberPage toMemberPage(
            PageResult<dev.mgvinuesa.family360.family.domain.FamilyMember> page
    ) {
        List<FamilyMember> data = page.data().stream().map(FamilyApiMapper::toApi).toList();
        return new FamilyMemberPage(
                page.page(),
                page.limit(),
                Math.toIntExact(page.totalElements()),
                page.totalPages(),
                data
        );
    }
}
