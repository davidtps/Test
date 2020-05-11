

var CONTOUR_AREA_THRESHOLD_MIN = 0.2
var CONTOUR_AREA_THRESHOLD_MAX = 0.95

var CURRENT_STEP = 1

var CONTOUR_AREA_THRESHOLD_MAX_2 = 1

var LENGTH_WEIGHT_RATIO_THREHOLD = 3

var WH_RADIO = 16/9;

//


function getThresholdTvImageMat(img,dst){
		console.log('--------getThresholdTvImageMat--------');
		let tvImage = getTvImageMat(img,dst);
		if(tvImage!=null){
			console.log('--------tvImage--------'+tvImage);
			cv.cvtColor(tvImage, tvImage, cv.COLOR_RGBA2GRAY, 0);			
			var val = 21; 
			var constt = 7;
			cv.adaptiveThreshold(tvImage, tvImage,255, cv.ADAPTIVE_THRESH_MEAN_C, cv.THRESH_BINARY, val, constt);
		}
		return tvImage;
}


function getTvImageMat(img,dst){
		//获取所有的轮廓
		let contours = findAllContours(img);
		//根据轮廓获取带边框的图像
		var imgh = img.size().height;
        var imgw = img.size().width;
		let tvImageMat = getTvMatFromContours(contours,dst,imgw,imgh);
		console.log('--------getTvImageMat--------'+tvImageMat);
		if(tvImageMat!=null){
			//console.table(tvImageMat);
			//调整大小
			let dsize = new cv.Size(400, 225);
			// You can try more different parameters
			cv.resize(tvImageMat, tvImageMat, dsize, 0, 0, cv.INTER_AREA);
		}
		return tvImageMat;
}


function blurImg(src){
	let ksize = new cv.Size(5, 5);
    cv.GaussianBlur(src, src, ksize, 3, 3, cv.BORDER_DEFAULT);	
}

//删除多余的不合格图像，只获取带边框的TV
function getTvContour(contours,minArea, maxArea,isNeedBounds){
        if(contours==null||contours.size()<1){
			return null;
		}
		
		console.log('removeNoiseContours-'+minArea+'--'+maxArea);
	    let out = new cv.MatVector() ; 
		var mArea = 0;
		let tvContour = null ; 
		var tvCounts =0;
		for (let i =0;i<contours.size();i++){
			let area = cv.contourArea(contours.get(i))
			if (area < minArea || area > maxArea){
				continue;
			}
			var rotatedRect = cv.minAreaRect(contours.get(i));
			
			// let wAndH = cv.RotatedRect.points(rotatedRect);
			let wAndH = rotatedRect.size;
			if (wAndH.width / wAndH.height > LENGTH_WEIGHT_RATIO_THREHOLD 
				|| wAndH.height / wAndH.width > LENGTH_WEIGHT_RATIO_THREHOLD){
					console.log('contours.size()-wAndH-remove--'+i+'---width-----'+wAndH.width+'----height-----'+wAndH.height);
					continue;
			}
			
			if(contours.get(i).rows!=4){
				continue;
			}
			
			tvCounts = tvCounts+1;
			//一般Tv都有边框，有可能大概率获取到2个所以获取矩形，根据isNeedBounds一个是内矩形不带边框的，一个外矩形带边框的 
			console.log('--------area--------'+area+'------mArea-----'+mArea);
			if(isNeedBounds){
				if(area>mArea){
					mArea =area;
					tvContour = contours.get(i);
				}
			}else{
				if(mArea==0||area<mArea){
					mArea =area;
					tvContour = contours.get(i);
				}
			}
			
			//console.table(tvContour);
		}
		console.log('--------tvCounts--------'+tvCounts);
		return tvContour;
}




//获取闭合的轮廓
function findAllContours(img){

	//图像灰阶
	cv.cvtColor(img, img, cv.COLOR_RGBA2GRAY, 0);
    blurImg(img);	
	//let M = cv.Mat.ones(5, 5, cv.CV_8U);
	//let anchor = new cv.Point(-1, -1);
	//膨胀腐蚀
	//cv.morphologyEx(img, img, cv.MORPH_OPEN, M, anchor, 1,cv.BORDER_CONSTANT, cv.morphologyDefaultBorderValue());
	//cv.morphologyEx(img, img, cv.MORPH_CLOSE, M, anchor, 1,cv.BORDER_CONSTANT, cv.morphologyDefaultBorderValue());		
	// You can try more different parameters
	//M.delete();
	//二值化
	var val = 21; 
	var constt = 7;
	cv.adaptiveThreshold(img, img,255, cv.ADAPTIVE_THRESH_MEAN_C, cv.THRESH_BINARY, val, constt);
	
	//cv.Canny(img, img, 500, 10, 3, false);
	console.log('img.cols'+img.cols);
	console.log('img.rows'+img.rows);
	//let dst = cv.Mat.zeros(img.rows,img.cols, cv.CV_8UC3);
	let contours = new cv.MatVector();
	let hierarchy = new cv.Mat();
	//获取轮廓
	cv.findContours(img, contours, hierarchy, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE);
	// draw contours with random Scalar
	console.log('contours.size()--start--'+contours.size());
	let poly = new cv.MatVector();
	if(contours.size()>0){
		for(let i =0;i<contours.size();i++){
			let matOut = new cv.Mat();
			//多边拟合，删除近似点
			cv.approxPolyDP(contours.get(i), matOut, 0.02 * cv.arcLength(contours.get(i), true), true);	
			poly.push_back(matOut);
			matOut.delete();
		}
		console.log('contours.size()---end-'+contours.size());
	}else{
		return null;
	}					 
	contours.delete(); hierarchy.delete();
	
	return poly;
	 
}

//获取带边框，并变正的TV图像矩阵
function getTvMatFromContours(contours,src,imgw,imgh){
	//获取带边框的Tv轮廓
	let maxContour =  getTvContour(contours,imgh * imgw * CONTOUR_AREA_THRESHOLD_MIN, imgh * imgw * CONTOUR_AREA_THRESHOLD_MAX,true);
	//console.table(maxContour);
	if(maxContour !=null){
		//四个顶点的二维数组
		let srcArray =[ [maxContour.data32S[0],maxContour.data32S[1]],[maxContour.data32S[2],maxContour.data32S[3]],[maxContour.data32S[4],maxContour.data32S[5]],[maxContour.data32S[6],maxContour.data32S[7]]];
		//按x坐标排序
		srcArray.sort(function(x, y){return x[0]-y[0];});
		//获取4个点
		let lt,ld,rt,rd ;
		//左侧两个点的坐标判断
		if(srcArray[0][1]<srcArray[1][1]){
			lt = srcArray[1];
			ld = srcArray[0];
		}else{
			lt = srcArray[0];
			ld = srcArray[1];
		}
		//右侧两个点的坐标判断
		if(srcArray[2][1]<srcArray[3][1]){
			rt = srcArray[3];
			rd = srcArray[2];
		}else{
			rt = srcArray[2];
			rd = srcArray[3];
		}
		//获取上下宽，按16:9换算高度。
		let widthA = Math.sqrt(Math.pow((rd[0]-ld[0]),2)+Math.pow((rd[1]-ld[1]),2));
		let widthB = Math.sqrt(Math.pow((rt[0]-lt[0]),2)+Math.pow((rt[1]-lt[1]),2));
		let srcWidth = Math.floor((widthA+widthB)/2);
		let srcHeight =Math.floor(srcWidth / WH_RADIO);
		console.log(srcHeight+'-----'+srcWidth);
		//源图像矩阵，根据左下顶点，右下顶点，左上顶点，右上顶点数组变换为矩阵。
		let srcTri = cv.matFromArray(4, 1, cv.CV_32FC2, [ld[0], ld[1],rd[0],rd[1],lt[0],lt[1],rt[0],rt[1]]);
		//要转换的目标图像矩阵，目标的左下顶点，右下顶点，左上顶点，右上顶点坐标数组变换为矩阵
		let dstTri = cv.matFromArray(4, 1, cv.CV_32FC2, [0,0,srcWidth-1,0,0,srcHeight-1,srcWidth-1,srcHeight-1]);
		let dst = new cv.Mat();	
        //目标大小		
		let dsize = new cv.Size(srcWidth, srcHeight);
		//变化矩阵
		let M = cv.getPerspectiveTransform(srcTri, dstTri);
		// 仿射变换，获取TV的Mat矩阵
		cv.warpPerspective(src, dst, M, dsize, cv.INTER_LINEAR, cv.BORDER_CONSTANT, new cv.Scalar());
		//src.delete();M.delete();srcTri.delete(); dstTri.delete();
		return dst;
	}
	return null;
}









//以下为测试方法











function removeNoiseContours(contours,minArea, maxArea){
        if(contours==null||contours.size()<1){
			return null;
		}
		
		console.log('removeNoiseContours-'+minArea+'--'+maxArea);
	    let out = new cv.MatVector() ; 
		var maxArea = 0;
		let maxContour; 
		for (let i =0;i<contours.size();i++){
			console.log('i---'+i);
			area = cv.contourArea(contours.get(i))
			console.log('removeNoiseContours--area-'+area+'---'+minArea+'--'+maxArea);
			if (area < minArea || area > maxArea){
				continue;
			}
			var rotatedRect = cv.minAreaRect(contours.get(i));
			/*let wAndH = cv.RotatedRect.points(rotatedRect);
			console.log('contours.size()-wAndH--x'+wAndH[0].x+'--y--'+wAndH[0].y+'---[1]x-'+wAndH[1].x+'--[1]y--'+wAndH[1].y+'------(wAndH[0].x / wAndH[1].x --'+wAndH[0].x / wAndH[0].y );
			if (wAndH[0].x / wAndH[0] > LENGTH_WEIGHT_RATIO_THREHOLD || wAndH[0].y / wAndH[0].x > LENGTH_WEIGHT_RATIO_THREHOLD){
					console.log('contours.size()-wAndH-remove--'+i);
					continue;
			}*/
			out.push_back(contours.get(i));
		}
		console.log('out-'+out.size());
		contours = out;
		return contours;
}



function findContoursTest(img){
    var imgh = img.size().height;
    var imgw = img.size().width;
	cv.cvtColor(img, img, cv.COLOR_RGBA2GRAY, 0);
    blurImg(img);
	
	
     var result = [];
    
     var saveCount = 0;
    
	// cv.adaptiveThreshold(img, img, 255, cv.ADAPTIVE_THRESH_GAUSSIAN_C, cv.THRESH_BINARY, 3, 2);
	let M = cv.Mat.ones(5, 5, cv.CV_8U);
	let anchor = new cv.Point(-1, -1);
	cv.morphologyEx(img, img, cv.MORPH_OPEN, M, anchor, 1,cv.BORDER_CONSTANT, cv.morphologyDefaultBorderValue());
	cv.morphologyEx(img, img, cv.MORPH_CLOSE, M, anchor, 1,cv.BORDER_CONSTANT, cv.morphologyDefaultBorderValue());		
	// You can try more different parameters
	M.delete();
	
	  var val = 21; 
	  var constt = 7;
	
	cv.adaptiveThreshold(img, img,255, cv.ADAPTIVE_THRESH_MEAN_C, cv.THRESH_BINARY, val, constt);
	
	//cv.Canny(img, img, 500, 10, 3, false);
	console.log('img.cols'+img.cols);
	console.log('img.rows'+img.rows);
	//let dst = cv.Mat.zeros(img.rows,img.cols, cv.CV_8UC3);
	let contours = new cv.MatVector();
	let hierarchy = new cv.Mat();
	cv.findContours(img, contours, hierarchy, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE);
	// draw contours with random Scalar
	console.log('contours.size()----'+contours.size());
	let poly = new cv.MatVector();
	if(contours.size()>0){
		for(let i =0;i<contours.size();i++){
			let matOut = new cv.Mat();
			cv.approxPolyDP(contours.get(i), matOut, 0.02 * cv.arcLength(contours.get(i), true), true);	
			console.log("-------- 0.02 * cv.arcLength(cnt, true)-----"+ 0.02 * cv.arcLength(contours.get(i), true))
			poly.push_back(matOut);
			matOut.delete();
		}
		console.log('contours.size()--removeNoiseContours-start-'+contours.size());
		poly = removeNoiseContoursTest(poly,imgh * imgw * CONTOUR_AREA_THRESHOLD_MIN, imgh * imgw * CONTOUR_AREA_THRESHOLD_MAX);
		console.log('contours.size()--removeNoiseContours-end-');
	}
	
	console.log('poly.size()----'+poly.size());
	let dst = cv.Mat.zeros(img.rows, img.cols, cv.CV_8UC3);
		
		
	let imgElement = document.getElementById('imageSrc');
	let mat = cv.imread(imgElement);
	for (let i = 0; i < poly.size(); i++) {
		let color = new cv.Scalar(Math.round(Math.random() * 255), Math.round(Math.random() * 255),
								  Math.round(Math.random() * 255));

		cv.drawContours(mat, poly,i, color, 1, cv.LINE_8, hierarchy, 0);
		let reshapePoint  = poly.get(i);
		cv.circle(mat, new cv.Point(reshapePoint.data32S[0],reshapePoint.data32S[1]), 2, color,
			3, cv.LINE_8);
			cv.circle(mat, new cv.Point(reshapePoint.data32S[2],reshapePoint.data32S[3]), 5, color,
			3, cv.LINE_8);
			cv.circle(mat, new cv.Point(reshapePoint.data32S[4],reshapePoint.data32S[5]), 10, color,
			3, cv.LINE_8);
			cv.circle(mat, new cv.Point(reshapePoint.data32S[6],reshapePoint.data32S[7]), 16, color,
			3, cv.LINE_8);
		//console.table(reshapePoint);
		//console.table(reshapePoint.data32S[0]);
		
	}
	cv.imshow('canvasOutput', mat);
	dst.delete(); contours.delete(); hierarchy.delete();mat.delete();poly.delete();


	 return contours;
	 
}


// function screen_perspective_transform(img, pts){
    
    // rect = order_points(pts)
    // (tl, tr, br, bl) = rect
    // #print(rect)
    // # compute the width of the new image, which will be the
    // # maximum distance between bottom-right and bottom-left
    // # x-coordiates or the top-right and top-left x-coordinates
    // widthA = np.sqrt(((br[0] - bl[0]) ** 2) + ((br[1] - bl[1]) ** 2))
    // widthB = np.sqrt(((tr[0] - tl[0]) ** 2) + ((tr[1] - tl[1]) ** 2))
    // norWidth = int((widthA + widthB) / 2)
    
    // # The height calc using norWidth and WH_RDAIO
    
    // norHeight = int((norWidth / WH_RADIO))
    
    // # compute the height of the new image, which will be the
    // # maximum distance between the top-right and bottom-right
    // # y-coordinates or the top-left and bottom-left y-coordinates
    // '''
    // heightA = np.sqrt(((tr[0] - br[0]) ** 2) + ((tr[1] - br[1]) ** 2))
    // heightB = np.sqrt(((tl[0] - bl[0]) ** 2) + ((tl[1] - bl[1]) ** 2))
    // maxHeight = max(int(heightA), int(heightB))
    // '''
    
    // # now that we have the dimensions of the new image, construct
    // # the set of destination points to obtain a "birds eye view",
    // # (i.e. top-down view) of the image, again specifying points
    // # in the top-left, top-right, bottom-right, and bottom-left
    // # order
    // dst = np.array([
        // [0, 0],
        // [norWidth - 1, 0],
        // [norWidth - 1, norHeight - 1],
        // [0, norHeight - 1]], dtype="float32")
    // #print(dst)
    // # compute the perspective transform matrix and then apply it
    // M = cv2.getPerspectiveTransform(rect, dst)
    // warped = cv2.warpPerspective(img, M, (norWidth, norHeight))

    // # return the warped image
    // return warped
// }
