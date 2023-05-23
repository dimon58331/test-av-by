import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

const TRANSPORT_API = "http://localhost:8080/api/transport";

@Injectable({
  providedIn: 'root'
})
export class TransportService {

  constructor(private http: HttpClient) { }

  public getTransportBrands(page: number, size: number): Observable<any> {
    let queryParameters = new HttpParams().append("page", page).append("size", size);
    return this.http.get(TRANSPORT_API + '/all/brand', {params: queryParameters});
  }

  public getTransportModelsByTransportBrandId(page: number, size: number, transportBrandId: number): Observable<any> {
    let queryParameters = new HttpParams().append("page", page).append("size", size)
      .append("transportBrandId", transportBrandId);
    return this.http.get(TRANSPORT_API + '/all/model', {params: queryParameters});
  }

  public getGenerationsTransportByTransportModelId(transportModelId: number): Observable<any> {
    let queryParameters = new HttpParams().append("transportModelId", transportModelId);
    return this.http.get(TRANSPORT_API + '/all/generation', {params: queryParameters});
  }

  public getTransportParametersByGenerationTransportId(generationTransportId: number): Observable<any> {
    let queryParameters = new HttpParams().append("generationTransportId", generationTransportId);
    return this.http.get(TRANSPORT_API + '/all/transportParameters', {params: queryParameters});
  }

  public getAllTransportParametersByHttpParameters(httpParameters: Map<string, string>): Observable<any> {
    let httpParams = new HttpParams();
    if (!httpParameters.get("page") && !httpParameters.get("size")) {
      httpParameters.set("page", "0");
      httpParameters.set("size", "25");
    }

    httpParameters.forEach((value, key) => {
      httpParams = httpParams.append(key, value);
    });

    console.log("Transport parameters http params: ");
    console.log(httpParams);

    return this.http.get(TRANSPORT_API + '/all', {params: httpParams});
  }

  public getAllMaxMinReleaseYears():Observable<any> {
    return this.http.get(TRANSPORT_API + '/all/releaseYears');
  }

  public getAllTransportParametersEnums(): Observable<any> {
    return this.http.get(TRANSPORT_API + '/all/transportParameters/enums');
  }
}
