import {Component, OnInit} from '@angular/core';
import {TransportService} from "../../service/transport.service";
import {TransportBrand} from "../../models/transport/TransportBrand";
import {TransportModel} from "../../models/transport/TransportModel";
import {GenerationTransport} from "../../models/transport/GenerationTransport";
import {ImageUploadService} from "../../service/image-upload.service";
import {TransportParameters} from "../../models/transport/TransportParameters";
import {PostService} from "../../service/post.service";
import {IndexComponent} from "../index/index.component";

const PAGE = 0;
const SIZE = 25;

@Component({
  selector: 'app-search-filter',
  templateUrl: './search-filter.component.html',
  styleUrls: ['./search-filter.component.css']
})
export class SearchFilterComponent implements OnInit{
  transportBrands: TransportBrand[];
  transportModels: TransportModel[];
  generationsTransport: GenerationTransport[];
  transportParameters: TransportParameters[];
  releaseYears: number[];
  transportParametersEnums: Map<string, Array<string>>;

  groupGenerationsTransport: Array<Array<GenerationTransport>>;

  isTransportBrandsLoaded = false;
  isTransportModelsLoaded = false;
  isGenerationsTransportLoaded = false;
  isTransportParametersLoaded = false;

  private endReleaseYear: number;
  private startReleaseYear: number;
  httpParams : Map<string, number>;
  transportParametersHttpParams: Map<string, number>;

  constructor(private transportService: TransportService, private imageService: ImageUploadService,
              private postService: PostService, private indexComponent: IndexComponent) {
    // @ts-ignore
    this.transportBrands = null;
    // @ts-ignore
    this.transportModels = null;
    // @ts-ignore
    this.generationsTransport = null;
    // @ts-ignore
    this.groupGenerationsTransport = null;
    // @ts-ignore
    this.transportParameters = null;
    // @ts-ignore
    this.releaseYears = null;
    // @ts-ignore
    this.endReleaseYear = null;
    // @ts-ignore
    this.startReleaseYear = null;

    this.httpParams = new Map<string, number>();
    this.transportParametersHttpParams = new Map<string, number>();
    this.transportParametersEnums = new Map<string, Array<string>>();
  }

  ngOnInit(): void {
    this.transportService.getTransportBrands(PAGE, SIZE)
      .subscribe(value => {
        this.transportBrands = value.content;
        this.isTransportBrandsLoaded = true;
        console.log(this.transportBrands);
      });
    this.transportService.getAllMaxMinReleaseYears()
      .subscribe(value => {
        console.log(value);
        this.fillReleaseYears(value.maxEndReleaseYear, value.minStartReleaseYear);
      });
    this.transportService.getAllTransportParametersEnums()
      .subscribe(value => {
        this.transportParametersEnums.set("bodyTypes", value.bodyTypes);
        this.transportParametersEnums.set("transmissionTypes", value.transmissionTypes);
        this.transportParametersEnums.set("engineTypes", value.engineTypes);
        console.log(this.transportParametersEnums);
      })
  }

  makeIsTransportModelsLoadedFalse() {
    this.makeIsGenerationsTransportLoadedFalse();
    this.isTransportModelsLoaded = false;
    // @ts-ignore
    this.transportModels = null;
    this.addBrandIdToHttpParameters(0);
  }

  makeIsGenerationsTransportLoadedFalse() {
    this.makeIsTransportParametersLoadedFalse();
    this.isGenerationsTransportLoaded = false;
    // @ts-ignore
    this.generationsTransport = null;
    // @ts-ignore
    this.groupGenerationsTransport = null;
    this.addModelIdToHttpParameters(0);
  }

  makeIsTransportParametersLoadedFalse() {
    this.isTransportParametersLoaded = false;
    // @ts-ignore
    this.transportParameters = null;
    this.addGenerationIdToHttpParameters(0);
  }

  findTransportModelsByTransportBrandId(brandId: number){
    this.transportService.getTransportModelsByTransportBrandId(PAGE, SIZE, brandId).subscribe(value => {
      this.transportModels = value.content;
      this.isTransportModelsLoaded = true;
      this.makeIsGenerationsTransportLoadedFalse();
    });
  }

  findGenerationsTransportByTransportModelId(transportModelId: number) {
    this.transportService.getGenerationsTransportByTransportModelId(transportModelId)
      .subscribe(value => {
        this.generationsTransport = value;
        console.log(this.generationsTransport);
        this.generationsTransport.forEach(generationTransport => {
          this.imageService.getGenerationTransportImage(generationTransport.id).subscribe(file => {
            generationTransport.image = file.imageBytes;
          });
        })
        this.groupTransport(this.generationsTransport);
        this.isGenerationsTransportLoaded = true;
      }, error => {
        console.log(error);
        this.makeIsGenerationsTransportLoadedFalse();
      })
  }

  findTransportParametersByGenerationTransportId(generationTransportId: number) {
    this.transportService.getTransportParametersByGenerationTransportId(generationTransportId)
      .subscribe(value => {
        console.log(value);
        this.transportParameters = value;
        console.log("transport parameters");
        console.log(this.transportParameters);
        this.isTransportParametersLoaded = true;
      }, error => {
        console.log(error);
        this.makeIsTransportParametersLoadedFalse();
      })
  }

  getSelectedOrDefaultEndReleaseYear(): number {
    return this.endReleaseYear > 0 ? this.endReleaseYear : this.releaseYears[this.releaseYears.length - 1];
  }

  endReleaseYearSelected(endReleaseYear: number): void {
    console.log(endReleaseYear);
    // @ts-ignore
    this.endReleaseYear = endReleaseYear;
  }

  getSelectedOrDefaultStartReleaseYear(): number {
    return this.startReleaseYear > 0 ? this.startReleaseYear : this.releaseYears[0];
  }

  startReleaseYearSelected(startReleaseYear: number): void {
    console.log(startReleaseYear);
    // @ts-ignore
    this.startReleaseYear = startReleaseYear;
  }

  addBrandIdToHttpParameters(transportBrandId: number) {
    transportBrandId != 0
      ? this.httpParams.set("brandId", transportBrandId)
      : this.httpParams.delete("brandId");
    this.loadFilteredPosts();
  }

  addModelIdToHttpParameters(transportModelId: number) {
    transportModelId != 0
      ? this.httpParams.set("modelId", transportModelId)
      : this.httpParams.delete("modelId");
    this.loadFilteredPosts();
  }

  addGenerationIdToHttpParameters(generationTransportId: number) {
    generationTransportId != 0
      ? this.httpParams.set("generationId", generationTransportId)
      : this.httpParams.delete("generationId");
    this.loadFilteredPosts();
  }

  addStartReleaseYearToHttpParameters(startReleaseYear: number ) {
    console.log("Start release year: ");
    console.log(startReleaseYear);
    startReleaseYear != 0
      ? this.transportParametersHttpParams.set("minReleaseYear", startReleaseYear)
      : this.transportParametersHttpParams.delete("minReleaseYear");
    this.loadTransportParametersFilteredBySomeParameters();
  }

  addEndReleaseYearToHttpParameters(endReleaseYear: number ) {
    console.log("End release year: ");
    console.log(endReleaseYear);
    endReleaseYear != 0
      ? this.transportParametersHttpParams.set("maxReleaseYear", endReleaseYear)
      : this.transportParametersHttpParams.delete("maxReleaseYear");
    this.loadTransportParametersFilteredBySomeParameters();
  }

  showPosts() {
    this.indexComponent.loadFilteredPostsToPosts();
  }

  showPostsCount(): number {
    return this.indexComponent.filteredPosts ? this.indexComponent.filteredPosts.length : 0;
  }

  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  private loadFilteredPosts() {
    this.indexComponent.loadPostsByParameters(this.httpParams);
  }

  private loadTransportParametersFilteredBySomeParameters() {
     this.transportService.getAllTransportParametersByHttpParameters(this.transportParametersHttpParams)
       .subscribe(value => {
         console.log(value);
       });
  }

  private groupTransport(generationsTransport: GenerationTransport[]): void {
    let column = 3;
    let row = generationsTransport.length % column == 0
      ? generationsTransport.length / column
      : parseInt(String(generationsTransport.length / column)) + 1;

    this.groupGenerationsTransport = new Array<Array<GenerationTransport>>(row);
    for (let i = 0; i < row; i++) {
      this.groupGenerationsTransport[i] = new Array<GenerationTransport>();
    }

    for (let i = 0, currentIndex = 0; i < row; i++) {
      for (let j = 0; j < column  && currentIndex < generationsTransport.length; j++){
        this.groupGenerationsTransport[i][j] = generationsTransport[currentIndex++];
      }
    }
  }

  private fillReleaseYears(maxEndReleaseYear: number, minStartReleaseYear: number) {
    this.releaseYears = new Array<number>();
    while (minStartReleaseYear <= maxEndReleaseYear) {
      this.releaseYears.push(minStartReleaseYear++);
    }
    console.log(this.releaseYears);
  }
}
