import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfile } from 'app/shared/model/profile.model';

type EntityResponseType = HttpResponse<IProfile>;
type EntityArrayResponseType = HttpResponse<IProfile[]>;

@Injectable({ providedIn: 'root' })
export class ProfileService {
    private resourceUrl = SERVER_API_URL + 'api/profiles';

    constructor(private http: HttpClient) {}

    create(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        console.log(profile);
        return this.http
            .post<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        return this.http
            .put<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    findCurrentProfile(userId: number): Observable<EntityResponseType> {
        return this.http
            .get<IProfile>(`${this.resourceUrl}/findCurrentProfile/${userId}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(profile: IProfile): IProfile {
        const copy: IProfile = Object.assign({}, profile, {
            birthDate: profile.birthDate != null && profile.birthDate.isValid() ? profile.birthDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.birthDate = res.body.birthDate != null ? moment(res.body.birthDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((profile: IProfile) => {
            profile.birthDate = profile.birthDate != null ? moment(profile.birthDate) : null;
        });
        return res;
    }
}
